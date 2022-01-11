package com.hkuroko.mirai

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.Mirai
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.FileCacheStrategy
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import java.io.File

@Configuration
@EnableConfigurationProperties(MiraiProperties::class)
class MiraiAutoConfiguration {


    @Bean
    @ConditionalOnMissingBean
    fun bot(properties: MiraiProperties): Bot = runBlocking {


        val bot = BotFactory.newBot(properties.qq, properties.pwd, BotConfiguration {
            protocol = BotConfiguration.MiraiProtocol.ANDROID_PAD
            loadDeviceInfoJson(properties.deviceJson)
            contactListCache.friendListCacheEnabled = properties.disableContactCache
            contactListCache.groupMemberListCacheEnabled = properties.disableContactCache
            cacheDir = File(properties.cacheDir)
            autoReconnectOnForceOffline = properties.autoReconnectOnForceOffline
            reconnectionRetryTimes = properties.reconnectionRetryTimes
            highwayUploadCoroutineCount = properties.highwayUploadCoroutineCount
                .coerceAtLeast(Runtime.getRuntime().availableProcessors())
            loginSolver = VoidLoginSolver()
        })

        when (properties.fileCacheStrategy) {
            "TEMP" -> Mirai.FileCacheStrategy = FileCacheStrategy.TempCache()
            "MEMORY" -> Mirai.FileCacheStrategy = FileCacheStrategy.MemoryCache
        }

        if (properties.autoLogin) {
            bot.login()
        }

        return@runBlocking bot
    }

    @Bean
    fun registerEventListener(): ApplicationListener<ContextRefreshedEvent> {
        return RegisterEventListener()
    }
}

