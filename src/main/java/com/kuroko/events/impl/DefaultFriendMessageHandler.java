package com.kuroko.events.impl;

import com.kuroko.events.FriendMessageHandler;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.*;

@Slf4j
public class DefaultFriendMessageHandler implements FriendMessageHandler {

    @Override
    public void onMessage(FriendMessageEvent event) {
        log.info("[R] {}({}): {}", event.getSender().getNick(), event.getSender().getId(), event.getMessage().contentToString());
    }

    @Override
    public void beforeSendMessage(FriendMessagePreSendEvent event) {
        log.info("[S] {}({}): {}", event.getTarget().getNick(), event.getTarget().getId(), event.getMessage().contentToString());

    }

    @Override
    public void afterSendMessage(FriendMessagePostSendEvent event) {
    }

    @Override
    public void onRecall(MessageRecallEvent.FriendRecall event) {
        log.info("[E] {}({}) 撤回一条消息", event.getAuthor().getNick(), event.getAuthorId());
    }
}
