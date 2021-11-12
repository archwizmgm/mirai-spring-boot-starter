package com.kuroko.event;

import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.SingleMessage;

public interface FriendEventHandler {

    void onMessage(Friend sender, MessageChain messageChain);
    /**
     *
     * @param target 目标用户
     * @param message 单条消息或者消息链
     * @See {@link SingleMessage},{@link MessageChain}
     */
    void beforeSendMessage(Friend target, Message message);

    void afterSendMessage(Friend target, MessageChain message, MessageReceipt<Friend> receipt, Throwable exception);

    void onRecall(Friend operator);
}
