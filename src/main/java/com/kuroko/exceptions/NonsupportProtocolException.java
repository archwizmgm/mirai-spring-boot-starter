package com.kuroko.exceptions;

public class NonsupportProtocolException extends RuntimeException {

    public NonsupportProtocolException() {
        super("登录协议仅支持(2.8.0-RC): ANDROID_PHONE/ANDROID_PAD/ANDROID_WATCH/IPAD/MACOS");
    }

}
