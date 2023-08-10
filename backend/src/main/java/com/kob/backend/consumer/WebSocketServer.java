package com.kob.backend.consumer;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")//注意不要以'/'结尾
public class WebSocketServer {

    private Session session = null;
    private User user;
    private static ConcurrentHashMap<Integer,WebSocketServer>userConnectionInfo
            = new ConcurrentHashMap<>();

    private static UserMapper userMapper;

    //线程安全的Set作为匹配池
    private static CopyOnWriteArraySet<User>matchpool = new CopyOnWriteArraySet<>();
    private void startMatching(){
        System.out.println("start matching");
        matchpool.add(this.user);
        while (matchpool.size() >= 2){
            Iterator<User> iterator = matchpool.iterator();
            User user1 = iterator.next();
            User user2 = iterator.next();
            matchpool.remove(user1);
            matchpool.remove(user2);
            Game game = new Game(13,14,20);
            game.createMap();
            //分别给user1和user2传送消息告诉他们匹配成功了
            //通过user1的连接向user1发消息
            JSONObject resp1 = new JSONObject();
            resp1.put("event","start-matching");
            resp1.put("opponent_username",user2.getUsername());
            resp1.put("opponent_photo",user2.getPhoto());
            resp1.put("gamemap",game.getG());
            WebSocketServer webSocketServer1 = userConnectionInfo.get(user1.getId());//获取user1的连接
            webSocketServer1.sendMessage(resp1.toJSONString());

            //通过user2的连接向user2发消息
            JSONObject resp2 = new JSONObject();
            resp2.put("event","start-matching");
            resp2.put("opponent_username",user1.getUsername());
            resp2.put("opponent_photo",user1.getPhoto());
            resp2.put("gamemap",game.getG());
            WebSocketServer webSocketServer2 = userConnectionInfo.get(user2.getId());
            webSocketServer2.sendMessage(resp2.toJSONString());
        }
    }
    private void stopMatching(){
        System.out.println("stop matching");
        matchpool.remove(this.user);
    }
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }
    @OnOpen
    public void onOpen(Session session, @PathParam("token")String token) throws IOException{
        //建立链接时自动调用
        this.session = session;
        System.out.println("Connected");
        int userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);
        if( this.user != null)
        {
            userConnectionInfo.put(userId,this);
        }
        else {
            this.session.close();
        }
    }
    @OnClose
    public void onClose() {
        //关闭链接时自动调用
        System.out.println("Disconnected");
        if (this.user != null){
            userConnectionInfo.remove(this.user.getId());
            matchpool.remove(this.user);
        }
    }
    @OnMessage
    public void onMessage(String message,Session session) {//当做路由，用来分配任务
        // Server从Client接受信息时触发
        System.out.println("Receive message");
        JSONObject data = JSONObject.parseObject(message);//将字符串解析成JSON
        String event = data.getString("event");
        if("start-matching".equals(event)){//防止event为空的异常
            startMatching();
        }
        else if ("stop-matching".equals(event)) {
            stopMatching();
        }
    }
    @OnError
    public void onError(Session session,Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message){
        //Server发送消息
        synchronized (this.session){
            try {
                this.session.getBasicRemote().sendText(message);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
