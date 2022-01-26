# mirai-spring-boot-starter

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
    <version>1.1</version>
</dependency>
```

### 继承`SimpleListenerHost`

- 在类上添加`@Component`注解,
- 在处理事件的方法上添加`@EventHandler`注解

> Java 支持的方法类型，T 表示任何 Event 类型，更多信息请参考`SimpleListenerHost`源码注释
> - void onEvent(T)
> - @NotNull ListeningStatus onEvent(T)   // 禁止返回 null
>
> Event 类型参考：[Mirai事件列表](https://github.com/mamoe/mirai/blob/dev/docs/EventList.md)

```java
/**
 * 编码时会接触到很多 mirai core 原生 api，请务必阅读 mirai 官方的文档
 */
@Component
public class MyEventHandler extends SimpleListenerHost {

    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        // 你的代码
    }

    @EventHandler
    public void onEvent(FriendMessageEvent event) {
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

- 推荐直接使用 Mirai Android 登录账号后导出
  device.json. [下载](https://install.appcenter.ms/users/mzdluo123/apps/miraiandroid/distribution_groups/release)

-

其他方式请参考社区的说明: [无法登录的临时处理方案](https://mirai.mamoe.net/topic/223/%E6%97%A0%E6%B3%95%E7%99%BB%E5%BD%95%E7%9A%84%E4%B8%B4%E6%97%B6%E5%A4%84%E7%90%86%E6%96%B9%E6%A1%88)

### 声明

本项目基于mirai，遵守并使用mirai声明，使用本项目前请阅读相关声明

- [Mirai声明](https://github.com/mamoe/mirai#%E5%A3%B0%E6%98%8E)
- [Mirai许可证](https://github.com/mamoe/mirai#%E8%AE%B8%E5%8F%AF%E8%AF%81)
- [协议支持](https://github.com/mamoe/mirai#%E5%8D%8F%E8%AE%AE%E6%94%AF%E6%8C%81)