package com.kuroko.exception;

import net.mamoe.mirai.network.CustomLoginFailedException;

public class AutoLoginFailedException extends CustomLoginFailedException {

    public AutoLoginFailedException() {
        super(true, "自动登录失败(starter仅支持已登录device.json),请配置device.json");
    }

}
