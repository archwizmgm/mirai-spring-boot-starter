package com.kuroko;

import com.kuroko.events.FriendMessageHandler;
import com.kuroko.events.GroupMessageHandler;
import com.kuroko.events.impl.DefaultFriendMessageHandler;
import com.kuroko.events.impl.DefaultGroupMessageHandler;
import com.kuroko.exceptions.NonsupportProtocolException;
import com.kuroko.properties.MiraiProperties;
import com.kuroko.solver.VoidLoginSolver;
import com.kuroko.templates.MiraiTemplate;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.util.Arrays;
import java.util.Optional;

@Configuration
@EnableConfigurationProperties(MiraiProperties.class)
public class MiraiAutoConfiguration {

    @Autowired
    MiraiProperties properties;

    @Bean
    @ConditionalOnMissingBean
    FriendMessageHandler friendMessageHandler() {
        return new DefaultFriendMessageHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    GroupMessageHandler groupMessageHandler() {
        return new DefaultGroupMessageHandler();
    }

    @Bean("bot")
    Bot bot(FriendMessageHandler friendMessageHandler, GroupMessageHandler groupMessageHandler) {

        BotConfiguration botConfiguration = new BotConfiguration();
        botConfiguration.setProtocol(supportedProtocol(properties.getProtocol()));
        botConfiguration.fileBasedDeviceInfo();

        if (properties.isDisableContactCache()) {
            botConfiguration.disableContactCache();
        }
        botConfiguration.setLoginSolver(new VoidLoginSolver());

        Bot bot = BotFactory.INSTANCE.newBot(properties.getAccount(), properties.getPassword(), botConfiguration);

        // 别删了try/catch删了会挂,我也不知道为什么 :)
        try {
            bot.login();
        } catch (Exception e) {  // net.mamoe.mirai.network.WrongPasswordException
            throw new BeanCreationException("自动登录失败!");
        }

        bot.getEventChannel().filter(ev -> ev instanceof FriendMessageEvent).subscribeAlways(FriendMessageEvent.class, friendMessageHandler::onMessage);
        bot.getEventChannel().filter(ev -> ev instanceof FriendMessagePreSendEvent).subscribeAlways(FriendMessagePreSendEvent.class, friendMessageHandler::beforeSendMessage);
        bot.getEventChannel().filter(ev -> ev instanceof FriendMessagePostSendEvent).subscribeAlways(FriendMessagePostSendEvent.class, friendMessageHandler::afterSendMessage);
        bot.getEventChannel().filter(ev -> ev instanceof MessageRecallEvent.FriendRecall).subscribeAlways(MessageRecallEvent.FriendRecall.class, friendMessageHandler::onRecall);

        bot.getEventChannel().filter(ev -> ev instanceof GroupMessageEvent).subscribeAlways(GroupMessageEvent.class, groupMessageHandler::onMessage);
        bot.getEventChannel().filter(ev -> ev instanceof GroupMessagePreSendEvent).subscribeAlways(GroupMessagePreSendEvent.class, groupMessageHandler::beforeSendMessage);
        bot.getEventChannel().filter(ev -> ev instanceof GroupMessagePostSendEvent).subscribeAlways(GroupMessagePostSendEvent.class, groupMessageHandler::afterSendMessage);
        bot.getEventChannel().filter(ev -> ev instanceof MessageRecallEvent.GroupRecall).subscribeAlways(MessageRecallEvent.GroupRecall.class, groupMessageHandler::onRecall);

        return bot;
    }

    @Bean
    @DependsOn("bot")
    MiraiTemplate miraiTemplate(Bot bot) {
        return new MiraiTemplate(bot);
    }

    private BotConfiguration.MiraiProtocol supportedProtocol(String str) {
        Optional<BotConfiguration.MiraiProtocol> protocol = Arrays.stream(BotConfiguration.MiraiProtocol.values())
                .filter(p -> p.toString().equals(str)).findFirst();
        if (protocol.isPresent()) {
            return protocol.get();
        } else {
            throw new NonsupportProtocolException();
        }

    }

}
