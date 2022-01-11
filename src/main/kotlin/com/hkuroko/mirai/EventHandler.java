package com.hkuroko.mirai;

public interface EventHandler<T> {

    void onEvent(T event);

    void onException(Throwable e);

}
