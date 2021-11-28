package com.kuroko.event.impl;

import com.kuroko.event.GroupEventHandler;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.contact.NormalMember;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;


public class DefaultGroupEventHandler implements GroupEventHandler {

    @Override
    public void onMessage(Group group, Member sender, MessageChain messageChain) {

    }

    @Override
    public void afterSendMessage(Group target, MessageChain messageChain, MessageReceipt<Group> receipt, Throwable exception) {

    }

    @Override
    public void beforeSendMessage(Group target, Message message) {

    }

    @Override
    public void onRecall(Group group, Member operator) {

    }

    @Override
    public void onTempMessage(Group group, NormalMember sender, MessageChain messageChain) {

    }

    @Override
    public void beforeSendTempMessage(Group group, NormalMember target, Message message) {

    }

    @Override
    public void afterSendTempMessage(Group group, NormalMember target, MessageChain messageChain, MessageReceipt<NormalMember> receipt, Throwable exception) {

    }
}
