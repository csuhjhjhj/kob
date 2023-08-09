package com.kob.backend.consumer;


import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/websocket/{token}")//注意不要以'/'结尾
public class WebSocketServer {

    private Session session = null;
    private User user;
    private static ConcurrentHashMap<Integer,WebSocketServer>userConnectionInfo
            = new ConcurrentHashMap<>();

    private static UserMapper userMapper;
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }
    @OnOpen
    public void onOpen(Session session, @PathParam("token")String token) {
        //建立链接时自动调用
        this.session = session;
        System.out.println("Connected");
        int userId = Integer.parseInt(token);//假设token为userId
        this.user = userMapper.selectById(userId);
        userConnectionInfo.put(userId,this);
    }
    @OnClose
    public void onClose() {
        //关闭链接时自动调用
        System.out.println("Disconnected");
        if (this.user != null){
            userConnectionInfo.remove(this.user.getId());
        }
    }
    @OnMessage
    public void onMessage(String message,Session session) {
        // Server从Client接受信息时触发
        System.out.println("Receive message");
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
