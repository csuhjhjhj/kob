# kob

基于SpringBoot的AI对抗平台

一个支持联机对战的ai游戏对抗平台，其中包含pk界面，对战列表界面，排行榜界面，和登录注册界面。其中pk界面使用微服务来实现了匹配功能，websocket界面实现了实况直播功能，真人pk功能。对战列表界面具有录像回放功能，排行榜界面展示了bot列表。

## 项目结构

![image-20230824234010122](https://github.com/csuhjhjhj/images/blob/master/kob/%E9%A1%B9%E7%9B%AE%E6%9E%B6%E6%9E%84%E5%9B%BE.png)

### 实现模块
#### PK界面

##### 匹配界面(微服务)
![image-20230824234010122](https://github.com/csuhjhjhj/images/blob/master/kob/%E5%8C%B9%E9%85%8D%E7%95%8C%E9%9D%A2.png)

##### 实况直播(`websocket`)

##### 真人PK(`websocket`)
##### 匹配界面(微服务)
![image-20230824234010122](https://github.com/csuhjhjhj/images/blob/master/kob/%E5%8C%B9%E9%85%8D-%E5%AF%B9%E6%88%98.gif)

##### Bot代码执行(`微服务`)
![image-20230824234010122](https://github.com/csuhjhjhj/images/blob/master/kob/%E5%BE%AE%E6%9C%8D%E5%8A%A1.png)
#### 对战列表
![image-20230824234010122](https://github.com/csuhjhjhj/images/blob/master/kob/%E5%AF%B9%E6%88%98%E5%88%97%E8%A1%A8.png)
##### 录像回放
![image-20230824234010122](https://github.com/csuhjhjhj/images/blob/master/kob/%E5%AF%B9%E6%88%98%E5%88%97%E8%A1%A8.gif)

#### 用户中心
##### 注册
![image-20230824234010122](https://github.com/csuhjhjhj/images/blob/master/kob/%E6%B3%A8%E5%86%8C.png)
##### 登录
![image-20230824234010122](https://github.com/csuhjhjhj/images/blob/master/kob/%E7%99%BB%E5%BD%95.png)
##### 我的Bot
![image-20230824234010122](https://github.com/csuhjhjhj/images/blob/master/kob/%E6%88%91%E7%9A%84bot.png)
##### Bot的记录
![image-20230824234010122](https://github.com/csuhjhjhj/images/blob/master/kob/bot%E7%BC%96%E8%BE%91.png)
