package com.hkuroko.mirai

import kotlinx.coroutines.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.*
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.reflect.KClass
import kotlin.reflect.full.IllegalCallableAccessException
import kotlin.reflect.full.callSuspend
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.kotlinFunction

class RegisterEventListener : ApplicationListener<ContextRefreshedEvent> {
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        val ctx = event.applicationContext
        val channel = ctx.getBean(Bot::class.java).eventChannel
        val coroutineContext: CoroutineContext = EmptyCoroutineContext

        ctx.getBeansOfType(ListenerHost::class.java).forEach { (_, v) ->

            val jobOfListenerHost: Job?
            val coroutineContext0 = if (v is SimpleListenerHost) {
                val listenerCoroutineContext =
                    CoroutineExceptionHandler(v::handleException) + v.coroutineContext + SupervisorJob(v.coroutineContext[Job])
                val listenerJob = listenerCoroutineContext[Job]

                val rsp = listenerCoroutineContext.minusKey(Job) +
                        coroutineContext +
                        (listenerCoroutineContext[CoroutineExceptionHandler] ?: EmptyCoroutineContext)

                val registerCancelHook = when {
                    listenerJob === null -> false

                    // Registering cancellation hook is needless
                    // if [Job] of [EventChannel] is same as [Job] of [SimpleListenerHost]
                    (rsp[Job] ?: channel.defaultCoroutineContext[Job]) === listenerJob -> false

                    else -> true
                }

                jobOfListenerHost = if (registerCancelHook) {
                    listenerCoroutineContext[Job]
                } else {
                    null
                }
                rsp
            } else {
                jobOfListenerHost = null
                coroutineContext
            }

            for (method in v.javaClass.declaredMethods) {
                method.getAnnotation(EventHandler::class.java)?.let {
                    if (!Modifier.isStatic(method.modifiers)) {
                        val listener = method.registerEventHandler(v, channel, it, coroutineContext0)
                        // For [SimpleListenerHost.cancelAll]
                        jobOfListenerHost?.invokeOnCompletion { exception ->
                            listener.cancel(
                                when (exception) {
                                    is CancellationException -> exception
                                    is Throwable -> CancellationException(null, exception)
                                    else -> null
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


private fun Method.registerEventHandler(
    owner: Any,
    eventChannel: EventChannel<*>,
    annotation: EventHandler,
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
): Listener<Event> {
    this.isAccessible = true
    val kotlinFunction = kotlin.runCatching { this.kotlinFunction }.getOrNull()
    return if (kotlinFunction != null && declaringClass.getDeclaredAnnotation(Metadata::class.java) != null) {
        // kotlin functions

        val param = kotlinFunction.parameters
        when (param.size) {
            3 -> { // ownerClass, receiver, event
                check(param[1].type == param[2].type) { "Illegal kotlin function ${kotlinFunction.name}. Receiver and param must have same type" }
                check((param[1].type.classifier as? KClass<*>)?.isSubclassOf(Event::class) == true) {
                    "Illegal kotlin function ${kotlinFunction.name}. First param or receiver must be subclass of Event, but found ${param[1].type.classifier}"
                }
            }
            2 -> { // ownerClass, event
                check((param[1].type.classifier as? KClass<*>)?.isSubclassOf(Event::class) == true) {
                    "Illegal kotlin function ${kotlinFunction.name}. First param or receiver must be subclass of Event, but found ${param[1].type.classifier}"
                }
            }
            else -> error("function ${kotlinFunction.name} must have one Event param")
        }
        lateinit var listener: Listener<*>
        kotlin.runCatching {
            kotlinFunction.isAccessible = true
        }
        suspend fun callFunction(event: Event): Any? {
            try {
                return when (param.size) {
                    3 -> {
                        if (kotlinFunction.isSuspend) {
                            kotlinFunction.callSuspend(owner, event, event)
                        } else withContext(Dispatchers.IO) { // for safety
                            kotlinFunction.call(owner, event, event)
                        }

                    }
                    2 -> {
                        if (kotlinFunction.isSuspend) {
                            kotlinFunction.callSuspend(owner, event)
                        } else withContext(Dispatchers.IO) { // for safety
                            kotlinFunction.call(owner, event)
                        }
                    }
                    else -> error("stub")
                }
            } catch (e: IllegalCallableAccessException) {
                listener.completeExceptionally(e)
                return ListeningStatus.STOPPED
            } catch (e: Throwable) {
                throw ExceptionInEventHandlerException(event, cause = e)
            }
        }
        require(!kotlinFunction.returnType.isMarkedNullable) {
            "Kotlin event handlers cannot have nullable return type."
        }
        require(kotlinFunction.parameters.none { it.type.isMarkedNullable }) {
            "Kotlin event handlers cannot have nullable parameter type."
        }
        when (kotlinFunction.returnType.classifier) {
            Unit::class, Nothing::class -> {
                eventChannel
                    .subscribeAlways(
                        param[1].type.classifier as KClass<out Event>,
                        coroutineContext,
                        annotation.concurrency,
                        annotation.priority
                    ) {
                        if (annotation.ignoreCancelled) {
                            if ((this as? CancellableEvent)?.isCancelled != true) {
                                callFunction(this)
                            }
                        } else callFunction(this)
                    }.also { listener = it }
            }
            ListeningStatus::class -> {
                eventChannel.subscribe(
                    param[1].type.classifier as KClass<out Event>,
                    coroutineContext,
                    annotation.concurrency,
                    annotation.priority
                ) {
                    if (annotation.ignoreCancelled) {
                        if ((this as? CancellableEvent)?.isCancelled != true) {
                            callFunction(this) as ListeningStatus
                        } else ListeningStatus.LISTENING
                    } else callFunction(this) as ListeningStatus
                }.also { listener = it }
            }
            else -> error("Illegal method return type. Required Void, Nothing or ListeningStatus, found ${kotlinFunction.returnType.classifier}")
        }
    } else {
        // java methods

        val paramType = this.parameterTypes[0]
        check(this.parameterTypes.size == 1 && Event::class.java.isAssignableFrom(paramType)) {
            "Illegal method parameter. Required one exact Event subclass. found ${this.parameterTypes.contentToString()}"
        }
        suspend fun callMethod(event: Event): Any? {
            fun Method.invokeWithErrorReport(self: Any?, vararg args: Any?): Any? = try {
                invoke(self, *args)
            } catch (exception: IllegalArgumentException) {
                throw IllegalArgumentException(
                    "Internal Error: $exception, method=${this}, this=$self, arguments=$args, please report to https://github.com/mamoe/mirai",
                    exception
                )
            } catch (e: Throwable) {
                throw ExceptionInEventHandlerException(event, cause = e)
            }


            return if (annotation.ignoreCancelled) {
                if ((event as? CancellableEvent)?.isCancelled != true) {    // 这里改动了
                    withContext(Dispatchers.IO) {
                        this@registerEventHandler.invokeWithErrorReport(owner, event)
                    }
                } else ListeningStatus.LISTENING
            } else withContext(Dispatchers.IO) {
                this@registerEventHandler.invokeWithErrorReport(owner, event)
            }
        }

        when (this.returnType) {
            Void::class.java, Void.TYPE, Nothing::class.java -> {
                eventChannel.subscribeAlways(
                    paramType.kotlin as KClass<out Event>,
                    coroutineContext,
                    annotation.concurrency,
                    annotation.priority
                ) {
                    callMethod(this)
                }
            }
            ListeningStatus::class.java -> {
                eventChannel.subscribe(
                    paramType.kotlin as KClass<out Event>,
                    coroutineContext,
                    annotation.concurrency,
                    annotation.priority
                ) {
                    callMethod(this) as ListeningStatus?
                        ?: error("Java method EventHandler cannot return `null`: $this")
                }
            }
            else -> error("Illegal method return type. Required Void or ListeningStatus, but found ${this.returnType.canonicalName}")
        }
    }
}