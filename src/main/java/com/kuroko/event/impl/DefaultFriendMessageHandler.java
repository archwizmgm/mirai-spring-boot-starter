package com.kuroko.event.impl;

import com.kuroko.event.FriendMessageHandler;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.FriendMessagePostSendEvent;
import net.mamoe.mirai.event.events.FriendMessagePreSendEvent;
import net.mamoe.mirai.event.events.MessageRecallEvent;

public class DefaultFriendMessageHandler implements FriendMessageHandler {

    @Override
    public void onMessage(FriendMessageEvent event) {
    }

    @Override
    public void beforeSendMessage(FriendMessagePreSendEvent event) {
    }

    @Override
    public void afterSendMessage(FriendMessagePostSendEvent event) {
    }

    @Override
    public void onRecall(MessageRecallEvent.FriendRecall event) {
    }
}
