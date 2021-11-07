package com.kuroko.events;

import net.mamoe.mirai.event.events.*;

public interface GroupMessageHandler {
    void onMessage(GroupMessageEvent event);

    void beforeSendMessage(GroupMessagePreSendEvent event);

    void afterSendMessage(GroupMessagePostSendEvent event);

    void onRecall(MessageRecallEvent.GroupRecall event);
}
