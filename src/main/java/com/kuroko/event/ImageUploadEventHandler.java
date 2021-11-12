package com.kuroko.event;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.utils.ExternalResource;

public interface ImageUploadEventHandler {

    void beforeUpload(Contact target, ExternalResource source);

    void afterUpload(Contact target, ExternalResource source);
}
