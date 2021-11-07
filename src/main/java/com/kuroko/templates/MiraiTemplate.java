package com.kuroko.templates;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.MessageReceipt;
import net.mamoe.mirai.message.data.PlainText;
import org.springframework.util.Assert;

// 主动事件
public class MiraiTemplate {

    Bot bot;

    public MiraiTemplate(Bot bot) {
        this.bot = bot;
    }

    public MessageReceipt<Friend> sendFriendMessage(Long id, String message) {
        Friend friend = bot.getFriend(id);
        Assert.notNull(friend, "No such friend: " + id);
        return friend.sendMessage(new PlainText(message));
    }

    public MessageReceipt<Group> sendGroupMessage(Long id, String message) {
        Group group = bot.getGroup(id);
        Assert.notNull(group, "No such group: " + id);
        return group.sendMessage(new PlainText(message));
    }

//    public void sendImg() {
//        Friend friend = bot.getFriend(849283527);
//        URL url = this.convertURL("https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fdmimg.5054399.com%2Fallimg%2FNARUTOpicture%2Fguijiao%2F001.jpg&refer=http%3A%2F%2Fdmimg.5054399.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1638802161&t=c3533ba893e51e08875de4fb0053591e");
//        try (InputStream inputStream = url.openStream(); ExternalResource externalResource = ExternalResource.create(inputStream)) {
//            Image image = friend.uploadImage(externalResource);
//            friend.sendMessage(image);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private URL convertURL(String urlStr) {
//        URL url = null;
//        try {
//            url = new URL(urlStr);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            // LOG.info("Unable to convert URL.");
//        }
//        return url;
//    }

}
