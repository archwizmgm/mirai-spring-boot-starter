package com.kuroko.event.impl;

import com.kuroko.event.GroupMessageHandler;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.GroupMessagePostSendEvent;
import net.mamoe.mirai.event.events.GroupMessagePreSendEvent;
import net.mamoe.mirai.event.events.MessageRecallEvent;


public class DefaultGroupMessageHandler implements GroupMessageHandler {

    @Override
    public void onMessage(GroupMessageEvent event) {
    }

    @Override
    public void beforeSendMessage(GroupMessagePreSendEvent event) {
    }

    @Override
    public void afterSendMessage(GroupMessagePostSendEvent event) {
    }

    @Override
    public void onRecall(MessageRecallEvent.GroupRecall event) {
    }
}
