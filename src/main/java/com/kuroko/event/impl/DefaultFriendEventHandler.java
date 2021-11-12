package com.kuroko.event.impl;

import com.kuroko.event.FriendEventHandler;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;

public class DefaultFriendEventHandler implements FriendEventHandler {


    @Override
    public void onMessage(Friend sender, MessageChain messageChain) {

    }

    @Override
    public void beforeSendMessage(Friend target, Message message) {

    }

    @Override
    public void afterSendMessage(Friend target, MessageChain message, MessageReceipt<Friend> receipt, Throwable exception) {

    }

    @Override
    public void onRecall(Friend operator) {

    }
}
