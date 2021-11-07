package com.kuroko.events;

import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.FriendMessagePostSendEvent;
import net.mamoe.mirai.event.events.FriendMessagePreSendEvent;
import net.mamoe.mirai.event.events.MessageRecallEvent;

public interface FriendMessageHandler {

    void onMessage(FriendMessageEvent event);

    void beforeSendMessage(FriendMessagePreSendEvent event);

    void afterSendMessage(FriendMessagePostSendEvent event);

    void onRecall(MessageRecallEvent.FriendRecall event);

}
