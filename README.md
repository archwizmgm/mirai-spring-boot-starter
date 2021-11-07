# mirai-spring-boot-starter
### 说明文档施工中……
### 目前仅有的功能
接收消息和发送文本消息(好友和群)。实现下面两个接口接收消息事件
- FriendMessageHandler
- GroupMessageHandler

发送功能，在使用的地方注入`MiraiTemplate`

### 使用注意
> 项目处于快速迭代阶段
1. 目前没有上传仓库，请下载项目，安装到本地maven仓库
2. 仅支持把在mirai登录过的账号device.json放在项目目录下自动登录
3. (上述操作正确的情况下)遇到版本过低可以加上`-Dmirai.slider.captcha.supported`试试
4. 其他登录问题，请到mirai社区寻找答案

### 配置
```properties
# 协议： ANDROID_PHONE/ANDROID_PAD/ANDROID_WATCH
mirai.protocol=ANDROID_PHONE
mirai.account=123456
mirai.password=pwd
# 生产环境建议禁用好友列表和群成员列表的缓存
mirai.disable-contact-cache=false
```