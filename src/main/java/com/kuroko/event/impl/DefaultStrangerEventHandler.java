package com.kuroko.event.impl;

import com.kuroko.event.StrangerEventHandler;
import net.mamoe.mirai.contact.Stranger;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;

public class DefaultStrangerEventHandler implements StrangerEventHandler {

    @Override
    public void onMessage(Stranger sender, MessageChain messageChain) {

    }

    @Override
    public void afterSendMessage(Stranger target, MessageChain messageChain, MessageReceipt<Stranger> receipt, Throwable exception) {

    }

    @Override
    public void beforeSendMessage(Stranger target, Message message) {

    }
}
