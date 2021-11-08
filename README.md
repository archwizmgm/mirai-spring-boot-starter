# mirai-spring-boot-starter
基于 [Mirai](https://github.com/mamoe/mirai) 及 [mirai-core](https://github.com/mamoe/mirai/blob/dev/docs/CoreAPI.md) 实现的 spring-boot-starter

实现"AI"居然需要9行

```java
@Component
public class MyMessageHandler implements FriendMessageHandler {
    @Override
    public void onMessage(FriendMessageEvent event) {
        String msg = event.getMessage().contentToString();
        String replay = msg.replace("吗", "").replace("?", "!").replace("？", "!");
        event.getSender().sendMessage(replay);
    }
}
```

## 声明
**一切开发旨在学习，请勿用于非法用途**

- 本项目完全免费且开放源代码，仅供学习和娱乐用途使用
- 本项目不会通过任何方式强制收取费用，或对使用者提出物质条件
- 本项目由本人自行开发维护，非官方支持
- 本项目采用`AGPLv3 `开源
- 其他声明请参考Mirai官方: 
[Mirai声明](https://github.com/mamoe/mirai#%E5%A3%B0%E6%98%8E) ，[Mirai许可证](https://github.com/mamoe/mirai#%E8%AE%B8%E5%8F%AF%E8%AF%81) ，[协议支持](https://github.com/mamoe/mirai#%E5%8D%8F%E8%AE%AE%E6%94%AF%E6%8C%81)

## 使用

**依赖**

> 1. 目前没有上传仓库，请下载项目，安装到本地maven仓库 : )
> 2. 不需要再引入mirai的其他依赖

```xml
<dependency>
   <groupId>com.kuroko</groupId>
   <artifactId>mirai-spring-boot-starter</artifactId>
   <version>0.0.1-SNAPSHOT</version>
</dependency>
```

**登录**

使用starter需要获取到以登录后的`device.json`放在项目根目录，或者程序启动目录

登录方法参考Mirai社区：[无法登录的临时处理方案](https://mirai.mamoe.net/topic/223/%E6%97%A0%E6%B3%95%E7%99%BB%E5%BD%95%E7%9A%84%E4%B8%B4%E6%97%B6%E5%A4%84%E7%90%86%E6%96%B9%E6%A1%88)

**配置**

```properties
# 协议：ANDROID_PHONE/ANDROID_PAD/ANDROID_WATCH
mirai.protocol=ANDROID_PHONE
mirai.account=123456
mirai.password=pwd # 暂时仅支持明文
```

**目前可用接口**

- FriendMessageHandler
- GroupMessageHandler

**MiraiTemplate**

```
// 在Bot上套了一层Optional方便写代码，不想用可以直接注入Bot
@Autowired
MiraiTemplate template; 
```

**可能遇到的问题**

`device.json`正确，配置正确，提示版本过低，jvm参数加上：

```
-Dmirai.slider.captcha.supported
```