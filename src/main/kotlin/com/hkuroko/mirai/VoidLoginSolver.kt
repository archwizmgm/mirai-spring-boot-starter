package com.hkuroko.mirai

import net.mamoe.mirai.Bot
import net.mamoe.mirai.utils.LoginSolver

class VoidLoginSolver : LoginSolver() {
    override suspend fun onSolvePicCaptcha(bot: Bot, data: ByteArray): String? {
        throw AutoLoginFailedException()
    }

    override suspend fun onSolveSliderCaptcha(bot: Bot, url: String): String? {
        throw AutoLoginFailedException()
    }

    override suspend fun onSolveUnsafeDeviceLoginVerify(bot: Bot, url: String): String? {
        throw AutoLoginFailedException()
    }
}