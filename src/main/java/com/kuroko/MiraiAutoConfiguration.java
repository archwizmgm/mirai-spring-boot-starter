package com.kuroko;

import com.kuroko.event.FriendMessageHandler;
import com.kuroko.event.GroupMessageHandler;
import com.kuroko.event.impl.DefaultFriendMessageHandler;
import com.kuroko.event.impl.DefaultGroupMessageHandler;
import com.kuroko.exception.NonsupportProtocolException;
import com.kuroko.propertie.MiraiProperties;
import com.kuroko.solver.VoidLoginSolver;
import com.kuroko.templates.MiraiTemplate;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Configuration
@EnableConfigurationProperties(MiraiProperties.class)
public class MiraiAutoConfiguration {

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
    @ConditionalOnMissingBean
    Bot bot(MiraiProperties properties, FriendMessageHandler friendMessageHandler, GroupMessageHandler groupMessageHandler) {

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

        // 好友相关事件
        bot.getEventChannel().filter(ev -> ev instanceof FriendMessageEvent).subscribeAlways(FriendMessageEvent.class,
                ev -> {
                    log.info("[R] {}({}): {}", ev.getSender().getNick(), ev.getSender().getId(), ev.getMessage().contentToString());
                    friendMessageHandler.onMessage(ev);
                });
        bot.getEventChannel().filter(ev -> ev instanceof FriendMessagePreSendEvent).subscribeAlways(FriendMessagePreSendEvent.class,
                ev -> {
                    log.info("[S] {}({}): {}", ev.getTarget().getNick(), ev.getTarget().getId(), ev.getMessage().contentToString());
                    friendMessageHandler.beforeSendMessage(ev);
                });
        bot.getEventChannel().filter(ev -> ev instanceof FriendMessagePostSendEvent).subscribeAlways(FriendMessagePostSendEvent.class, friendMessageHandler::afterSendMessage);
        bot.getEventChannel().filter(ev -> ev instanceof MessageRecallEvent.FriendRecall).subscribeAlways(MessageRecallEvent.FriendRecall.class,
                ev -> {
                    log.info("[E] {}({}) 撤回一条消息", ev.getAuthor().getNick(), ev.getAuthorId());
                    friendMessageHandler.onRecall(ev);
                });

        // 群相关事件
        bot.getEventChannel().filter(ev -> ev instanceof GroupMessageEvent).subscribeAlways(GroupMessageEvent.class,
                ev -> {
                    log.info("[R] [{}({})] {}({}): {}", ev.getGroup().getName(), ev.getGroup().getId(), ev.getSender().getNick(),
                            ev.getSender().getId(), ev.getMessage().contentToString());
                    groupMessageHandler.onMessage(ev);
                });
        bot.getEventChannel().filter(ev -> ev instanceof GroupMessagePreSendEvent).subscribeAlways(GroupMessagePreSendEvent.class,
                ev -> {
                    log.info("[S] [{}({})]: {}", ev.getTarget().getName(), ev.getTarget().getId(), ev.getMessage().contentToString());
                    groupMessageHandler.beforeSendMessage(ev);
                });
        bot.getEventChannel().filter(ev -> ev instanceof GroupMessagePostSendEvent).subscribeAlways(GroupMessagePostSendEvent.class, groupMessageHandler::afterSendMessage);
        bot.getEventChannel().filter(ev -> ev instanceof MessageRecallEvent.GroupRecall).subscribeAlways(MessageRecallEvent.GroupRecall.class,
                ev -> {
                    log.info("[E] [{}({})] {}({}) 撤回[{}({})]一条消息", ev.getGroup().getName(), ev.getGroup().getId(), ev.getOperator().getNick(),
                            ev.getOperator().getId(), ev.getAuthor().getNick(), ev.getAuthor().getId());
                    groupMessageHandler.onRecall(ev);
                });


        return bot;
    }

    @Bean
    @DependsOn("bot")
    @ConditionalOnMissingBean
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
