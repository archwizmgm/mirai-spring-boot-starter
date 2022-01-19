# mirai-spring-boot-starter

```java

@Component
public class MyHandler implements EventHandler<FriendMessageEvent> {

    @Override
    public void onEvent(FriendMessageEvent event) {
        String msg = event.getMessage().contentToString();
        String replay = msg.replace("吗", "").replace("?", "!").replace("？", "!");
        event.getSender().sendMessage(replay);
    }

    @Override
    public void onException(Throwable e) {
        // onEvent 抛出的异常时会调用该方法
    }
}
```

## 快速开始

### 安装到本地maven仓库

```shell
$ git clone https://github.com/hkuroko/mirai-spring-boot-starter.git
$ cd mirai-spring-boot-starter
$ mvn install
```

### 在项目中引入

```xml

<dependency>
    <groupId>com.github.hkuroko</groupId>
    <artifactId>mirai-spring-boot-starter</artifactId>
    <version>1.0</version>
</dependency>
```

### 实现接口`EventHandler`, 在类上添加`@Component`注解

> 接口上的泛型可以是Mirai中的任何事件，实现接口后会自动在Mirai注册
>
> 参考：[Mirai事件列表](https://github.com/mamoe/mirai/blob/dev/docs/EventList.md)

```java

@Component
public class MyHandler implements EventHandler<FriendMessageEvent> {

    @Override
    public void onEvent(FriendMessageEvent event) {
        // 你的代码
    }

    @Override
    public void onException(Throwable e) {
        // 你的代码
    }
}
```

### 启动项目

#### 登录

- 本项目设计上是一个`spring boot`只登录一个账号
- 默认情况下使用配置信息自动登录；自动登录失败会直接退出程序，不会触发Mirai的登录流程

```properties
mirai.qq=123456
mirai.pwd=password
mirai.device-json=/* Mirai 登录后生成的 device.json (json可以压缩后放在配置文件中) */
# 更多可用的配置见IDE中的提示
```

#### 获取 device.json

- 推荐直接使用 Mirai Android 登录账号后导出 device.json. [下载](https://install.appcenter.ms/users/mzdluo123/apps/miraiandroid/distribution_groups/release)
- 其他方式请参考社区的说明: [无法登录的临时处理方案](https://mirai.mamoe.net/topic/223/%E6%97%A0%E6%B3%95%E7%99%BB%E5%BD%95%E7%9A%84%E4%B8%B4%E6%97%B6%E5%A4%84%E7%90%86%E6%96%B9%E6%A1%88)