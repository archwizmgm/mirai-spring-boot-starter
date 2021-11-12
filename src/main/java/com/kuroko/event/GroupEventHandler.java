package com.kuroko.event;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;

public interface GroupEventHandler {

    void onMessage(Group group, Member sender, MessageChain messageChain);

    void afterSendMessage(Group target, MessageChain messageChain, MessageReceipt<Group> receipt, Throwable exception);

    void beforeSendMessage(Group target, Message message);

    void onRecall(Group group, Member operator);

    void onTempMessage(Group group, NormalMember sender, MessageChain messageChain);

    void beforeSendTempMessage(Group group, NormalMember target, Message message);

    void afterSendTempMessage(Group group, NormalMember target, MessageChain messageChain, MessageReceipt<NormalMember> receipt, Throwable exception);
}
