package com.kuroko.event;

import net.mamoe.mirai.contact.ClientKind;
import net.mamoe.mirai.contact.OtherClient;
import net.mamoe.mirai.message.data.MessageChain;

public interface OtherClientEventHandler {

    void onMessage(OtherClient client, MessageChain messageChain);

    void offline(OtherClient client);

    void online(OtherClient client, ClientKind kind);
}
