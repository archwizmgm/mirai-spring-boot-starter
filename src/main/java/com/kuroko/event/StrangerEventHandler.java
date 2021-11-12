package com.kuroko.event;

import net.mamoe.mirai.contact.Stranger;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;

public interface StrangerEventHandler {

    void onMessage(Stranger sender, MessageChain messageChain);

    void afterSendMessage(Stranger target, MessageChain messageChain, MessageReceipt<Stranger> receipt, Throwable exception);

    void beforeSendMessage(Stranger target, Message message);
}
