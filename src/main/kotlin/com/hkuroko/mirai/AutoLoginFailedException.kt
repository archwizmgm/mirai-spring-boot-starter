package com.hkuroko.mirai

import net.mamoe.mirai.network.CustomLoginFailedException

class AutoLoginFailedException(
    killBot: Boolean = true,
    message: String? = "Auto login failed,Please configure device-json in application.yml"
) : CustomLoginFailedException(killBot, message)