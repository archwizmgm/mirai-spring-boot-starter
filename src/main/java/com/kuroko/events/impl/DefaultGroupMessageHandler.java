package com.kuroko.events.impl;

import com.kuroko.events.GroupMessageHandler;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.*;

import java.util.Objects;

@Slf4j
public class DefaultGroupMessageHandler implements GroupMessageHandler {

    @Override
    public void onMessage(GroupMessageEvent event) {
        log.info("[R] [{}({})] {}({}): {}", event.getGroup().getName(), event.getGroup().getId(), event.getSender().getNick(),
                event.getSender().getId(), event.getMessage().contentToString());
    }

    @Override
    public void beforeSendMessage(GroupMessagePreSendEvent event) {
        log.info("[S] [{}({})]: {}", event.getTarget().getName(), event.getTarget().getId(), event.getMessage().contentToString());
    }

    @Override
    public void afterSendMessage(GroupMessagePostSendEvent event) {

    }

    @Override
    public void onRecall(MessageRecallEvent.GroupRecall event) {
        log.info("[E] [{}({})] {}({}) 撤回[{}({})]一条消息", event.getGroup().getName(), event.getGroup().getId(), event.getOperator().getNick(),
                event.getOperator().getId(), event.getAuthor().getNick(), event.getAuthor().getId());
    }
}
