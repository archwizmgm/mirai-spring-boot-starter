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
import java.util.Optional;


public class MiraiTemplate {

    Bot bot;
    HttpClient client;

    public MiraiTemplate(Bot bot,HttpClient client) {
        this.bot = bot;
        this.client = client;
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

    public Optional<Image> uploadImg(Contact contact, String url) {
        Image image = null;
        Optional<InputStream> inputStream = downloadFileAsInputStream(url);
        if (inputStream.isPresent()) {
            image = ExternalResource.uploadAsImage(inputStream.get(), contact);
        }
        return Optional.ofNullable(image);
    }

    public Optional<InputStream> downloadFileAsInputStream(String url) {
        URI uri = URI.create(url);
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
