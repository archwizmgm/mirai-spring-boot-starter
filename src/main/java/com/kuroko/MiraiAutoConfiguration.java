package com.kuroko;

import com.kuroko.event.*;
import com.kuroko.event.impl.*;
import com.kuroko.exception.NonsupportProtocolException;
import com.kuroko.propertie.MiraiProperties;
import com.kuroko.solver.VoidLoginSolver;
import com.kuroko.templates.MiraiTemplate;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.EventChannel;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Configuration
@Import(EventHandlerConfiguration.class)
@EnableConfigurationProperties(MiraiProperties.class)
public class MiraiAutoConfiguration {

    //todo 好像所有事件都可以取消
    @Bean("bot")
    @ConditionalOnMissingBean
    Bot bot(MiraiProperties properties, FriendEventHandler friend, GroupEventHandler group, StrangerEventHandler stranger, OtherClientEventHandler client,
            ImageUploadEventHandler image
    ) {

        BotConfiguration botConfiguration = new BotConfiguration();
        botConfiguration.setProtocol(supportedProtocol(properties.getProtocol()));
        botConfiguration.loadDeviceInfoJson(properties.getDevice());
        botConfiguration.setLoginSolver(new VoidLoginSolver());
        if (properties.isDisableContactCache()) {
            botConfiguration.disableContactCache();
        }

        Bot bot = BotFactory.INSTANCE.newBot(properties.getAccount(), properties.getPassword(), botConfiguration);

        // 别删了try/catch删了会挂,我也不知道为什么 :)
        try {
            bot.login();
        } catch (Exception e) {  // net.mamoe.mirai.network.WrongPasswordException
            throw new BeanCreationException("自动登录失败!");
        }

        EventChannel<BotEvent> eventChannel = bot.getEventChannel();

        // 日志
        // 好友相关事件
        eventChannel.subscribeAlways(FriendMessageEvent.class, ev -> log.info("[R] {}({}): {}", ev.getSender().getNick(), ev.getSender().getId(), ev.getMessage().contentToString()));
        eventChannel.subscribeAlways(FriendMessagePreSendEvent.class, ev -> log.info("[S] {}({}): {}", ev.getTarget().getNick(), ev.getTarget().getId(), ev.getMessage().contentToString()));
        eventChannel.subscribeAlways(MessageRecallEvent.FriendRecall.class, ev -> log.info("[E] {}({}) 撤回一条消息", ev.getAuthor().getNick(), ev.getAuthorId()));

        // 群相关事件
        eventChannel.subscribeAlways(GroupMessageEvent.class, ev -> log.info("[R] [{}({})] {}({}): {}", ev.getGroup().getName(), ev.getGroup().getId(), ev.getSender().getNick(), ev.getSender().getId(), ev.getMessage().contentToString()));
        eventChannel.subscribeAlways(GroupMessagePreSendEvent.class, ev -> log.info("[S] [{}({})]: {}", ev.getTarget().getName(), ev.getTarget().getId(), ev.getMessage().contentToString()));
        eventChannel.subscribeAlways(MessageRecallEvent.GroupRecall.class, ev -> log.info("[E] [{}({})] {}({}) 撤回[{}({})]一条消息", ev.getGroup().getName(), ev.getGroup().getId(), ev.getOperator() == null ? ev.getBot().getNick() : ev.getOperator().getNick(), ev.getOperator() == null ? ev.getBot().getId() : ev.getOperator().getId(), ev.getAuthor().getNick(), ev.getAuthor().getId()));

        // 群临时会话
        eventChannel.subscribeAlways(GroupTempMessageEvent.class, ev -> log.info("[R] [{}({})] {}({}): {}", ev.getGroup().getName(), ev.getGroup().getId(), ev.getSender().getNick(), ev.getSender().getId(), ev.getMessage().contentToString()));
        eventChannel.subscribeAlways(GroupTempMessagePreSendEvent.class, ev -> log.info("[S] [{}({})] {}({}): {}", ev.getGroup().getName(), ev.getGroup().getId(), ev.getTarget().getNick(), ev.getTarget().getId(), ev.getMessage().contentToString()));

        // 陌生人
        eventChannel.subscribeAlways(StrangerMessageEvent.class, ev -> log.info("[R] {}({},陌生人): {}", ev.getSender().getNick(), ev.getSender().getId(), ev.getMessage().contentToString()));
        eventChannel.subscribeAlways(StrangerMessagePreSendEvent.class, ev -> log.info("[S] {}({},陌生人): {}", ev.getTarget().getNick(), ev.getTarget().getId(), ev.getMessage().contentToString()));

        // 其他客户端
        eventChannel.subscribeAlways(OtherClientMessageEvent.class, ev -> log.info("[R] {}({}): {}", ev.getClient().getInfo().getDeviceName(), ev.getClient().getInfo().getDeviceKind(), ev.getMessage().contentToString()));

        // 事件处理器
        if (!(friend instanceof DefaultFriendEventHandler)) {
            // 好友相关事件
            eventChannel.subscribeAlways(FriendMessageEvent.class, ev -> friend.onMessage(ev.getSender(), ev.getMessage()));
            eventChannel.subscribeAlways(FriendMessagePreSendEvent.class, ev -> friend.beforeSendMessage(ev.getTarget(), ev.getMessage()));
            eventChannel.subscribeAlways(MessageRecallEvent.FriendRecall.class, ev -> friend.onRecall(ev.getOperator()));
            eventChannel.subscribeAlways(FriendMessagePostSendEvent.class, ev -> friend.afterSendMessage(ev.getTarget(), ev.getMessage(), ev.getReceipt(), ev.getException()));
        }

        if (!(group instanceof DefaultGroupEventHandler)) {
            // 群会话
            eventChannel.subscribeAlways(GroupMessageEvent.class, ev -> group.onMessage(ev.getGroup(), ev.getSender(), ev.getMessage()));
            eventChannel.subscribeAlways(GroupMessagePreSendEvent.class, ev -> group.beforeSendMessage(ev.getTarget(), ev.getMessage()));
            eventChannel.subscribeAlways(MessageRecallEvent.GroupRecall.class, ev -> group.onRecall(ev.getGroup(), ev.getOperator()));
            eventChannel.subscribeAlways(GroupMessagePostSendEvent.class, ev -> group.afterSendMessage(ev.getTarget(), ev.getMessage(), ev.getReceipt(), ev.getException()));

            // 群临时会话
            eventChannel.subscribeAlways(GroupTempMessageEvent.class, ev -> group.onTempMessage(ev.getGroup(), ev.getSender(), ev.getMessage()));
            eventChannel.subscribeAlways(GroupTempMessagePreSendEvent.class, ev -> group.beforeSendTempMessage(ev.getGroup(), ev.getTarget(), ev.getMessage()));
            eventChannel.subscribeAlways(GroupTempMessagePostSendEvent.class, ev -> group.afterSendTempMessage(ev.getGroup(), ev.getTarget(), ev.getMessage(), ev.getReceipt(), ev.getException()));
            // todo 群临时会话消息撤回
        }
        if (!(stranger instanceof DefaultStrangerEventHandler)) {
            // 陌生人会话
            eventChannel.subscribeAlways(StrangerMessageEvent.class, ev -> stranger.onMessage(ev.getSender(), ev.getMessage()));
            eventChannel.subscribeAlways(StrangerMessagePreSendEvent.class, ev -> stranger.beforeSendMessage(ev.getTarget(), ev.getMessage()));
            eventChannel.subscribeAlways(StrangerMessagePostSendEvent.class, ev -> stranger.afterSendMessage(ev.getTarget(), ev.getMessage(), ev.getReceipt(), ev.getException()));
            // todo 陌生人消息撤回
        }
        if (!(client instanceof DefaultOtherClientEventHandler)) {
            // 其他客户端
            eventChannel.subscribeAlways(OtherClientMessageEvent.class, ev -> client.onMessage(ev.getClient(), ev.getMessage()));
            eventChannel.subscribeAlways(OtherClientOfflineEvent.class, ev -> client.offline(ev.getClient()));
            eventChannel.subscribeAlways(OtherClientOnlineEvent.class, ev -> client.online(ev.getClient(), ev.getKind()));
        }

        // 图片上传前后 BeforeImageUploadEvent ImageUploadEvent
        if (!(image instanceof DefaultImageUploadEventHandler)) {
            eventChannel.subscribeAlways(BeforeImageUploadEvent.class, ev -> image.beforeUpload(ev.getTarget(), ev.getSource()));
            eventChannel.subscribeAlways(ImageUploadEvent.class, ev -> image.afterUpload(ev.getTarget(), ev.getSource()));
        }

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
