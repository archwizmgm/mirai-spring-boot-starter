package com.hkuroko.mirai

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "mirai")
data class MiraiProperties(
    val protocol: String = "ANDROID_PHONE",
    val qq: Long,
    val pwd: String,
    val deviceJson: String,
    val disableContactCache: Boolean = false,
    val fileCacheStrategy: String = "TEMP",
    val cacheDir: String = "cache",
    val autoReconnectOnForceOffline: Boolean = false,
    val reconnectionRetryTimes: Int = 5,
    val highwayUploadCoroutineCount: Int = 2,
    val autoLogin: Boolean = true
)