package com.hkuroko.mirai;

import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.Dispatchers;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.events.BotEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class RegisterEventListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext ctx = event.getApplicationContext();
        EventChannel<BotEvent> channel = ctx.getBean(Bot.class).getEventChannel();
        ctx.getBeansOfType(EventHandler.class).forEach((k, v) -> {
            Type[] types = v.getClass().getGenericInterfaces();
            ParameterizedType parameterizedType = (ParameterizedType) types[0];
            Class actualType = (Class) parameterizedType.getActualTypeArguments()[0];
            channel.exceptionHandler(throwable -> {
                        v.onException(throwable);
                        return null;
                    })
                    .subscribeAlways(actualType, (CoroutineContext) Dispatchers.getIO(), v::onEvent);
        });
    }
}
