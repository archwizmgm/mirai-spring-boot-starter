package com.kuroko.event.impl;

import com.kuroko.event.OtherClientEventHandler;
import net.mamoe.mirai.contact.ClientKind;
import net.mamoe.mirai.contact.OtherClient;
import net.mamoe.mirai.message.data.MessageChain;

public class DefaultOtherClientEventHandler implements OtherClientEventHandler {


    @Override
    public void onMessage(OtherClient client, MessageChain messageChain) {

    }

    @Override
    public void offline(OtherClient client) {

    }

    @Override
    public void online(OtherClient client, ClientKind kind) {

    }
}
