package com.kuroko.templates;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.*;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;


public class MiraiTemplate {

    Bot bot;

    public MiraiTemplate(Bot bot) {
        this.bot = bot;
    }

    public boolean isBotOnLine() {
        return bot.isOnline();
    }

    public void closeBot() {
        bot.close();
    }

    public void closeBot(Throwable e) {
        bot.close(e);
    }

    public void closeAndJoin(Throwable e) {
        bot.closeAndJoin(e);
    }

    public Friend getBotAsFriend() {
        return bot.getAsFriend();
    }

    public ContactList<OtherClient> otherClients() {
        return bot.getOtherClients();
    }

    public Optional<Friend> getFriend(Long id) {
        return Optional.ofNullable(bot.getFriend(id));
    }

    public Optional<ContactList<Friend>> getFriends() {
        return Optional.of(bot.getFriends());
    }

    public Optional<Group> getGroup(Long id) {
        return Optional.ofNullable(bot.getGroup(id));
    }

    public Optional<ContactList<Group>> getGroups() {
        return Optional.of(bot.getGroups());
    }

    public Optional<Stranger> getStranger(Long id) {
        return Optional.ofNullable(bot.getStranger(id));
    }

    public Optional<ContactList<Stranger>> getStrangers() {
        return Optional.of(bot.getStrangers());
    }

    public Optional<MessageReceipt<Contact>> sendImg(Contact contact, String url) {
        MessageReceipt<Contact> receipt = null;
        Optional<InputStream> inputStream = downloadFileAsInputStream(url);
        if (inputStream.isPresent()) {
            receipt = ExternalResource.sendAsImage(inputStream.get(), contact);
        }
        return Optional.ofNullable(receipt);
    }

    public Optional<Image> uploadImg(Contact contact, String url) {
        Image image = null;
        Optional<InputStream> inputStream = downloadFileAsInputStream(url);
        if (inputStream.isPresent()) {
            image = ExternalResource.uploadAsImage(inputStream.get(), contact);
        }
        return Optional.ofNullable(image);
    }

    //todo 通过代理下载
    public Optional<InputStream> downloadFileAsInputStream(String url) {
        URI uri = URI.create(url);
        HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(20)).build();
        HttpRequest request = HttpRequest.newBuilder(uri).build();
        HttpResponse<InputStream> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("下载文件失败!");
        }
        if (response != null) {
            return Optional.of(response.body());
        } else {
            return Optional.empty();
        }
    }

}
