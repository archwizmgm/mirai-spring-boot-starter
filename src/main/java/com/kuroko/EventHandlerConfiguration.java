package com.kuroko;

import com.kuroko.event.*;
import com.kuroko.event.impl.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventHandlerConfiguration {

    @Bean
    @ConditionalOnMissingBean
    FriendEventHandler friendMessageHandler() {
        return new DefaultFriendEventHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    GroupEventHandler groupMessageHandler() {
        return new DefaultGroupEventHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    StrangerEventHandler strangerEventHandler() {
        return new DefaultStrangerEventHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    OtherClientEventHandler otherClientEventHandler() {
        return new DefaultOtherClientEventHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    ImageUploadEventHandler imageUploadEventHandler() {
        return new DefaultImageUploadEventHandler();
    }
}
