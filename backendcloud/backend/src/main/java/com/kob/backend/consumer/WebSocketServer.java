package com.kob.backend.consumer;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
    public static ConcurrentHashMap<Integer,WebSocketServer>userConnectionInfo
            = new ConcurrentHashMap<>();
    private static UserMapper userMapper;
    private Game game = null;
    public static RecordMapper recordMapper;
    //线程安全的Set作为匹配池
    private static CopyOnWriteArraySet<User>matchpool = new CopyOnWriteArraySet<>();
    private final static String addPlayerUrl = "http://127.0.0.1:3001/player/add/";
    private final static String removePLayerUrl = "http://127.0.0.1:3001/player/remove/";
    private static RestTemplate restTemplate;
    private void startMatching(){
        System.out.println("开始匹配");
        MultiValueMap<String,String>data = new LinkedMultiValueMap<>();
        System.out.println(this.user.getId().toString());
        System.out.println(this.user.getRating().toString());

        data.add("user_id",this.user.getId().toString());
        data.add("rating",this.user.getRating().toString());
        //向MatchingSystem发请求
        restTemplate.postForObject(addPlayerUrl,data,String.class);
    }
    private void stopMatching(){
        System.out.println("stop matching");
        MultiValueMap<String,String>data = new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        restTemplate.postForObject(removePLayerUrl,data,String.class);
    }
    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        WebSocketServer.restTemplate = restTemplate;
    }
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper){
        WebSocketServer.recordMapper = recordMapper;
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
        System.out.println("Receive message!");
        JSONObject data = JSONObject.parseObject(message);//将字符串解析成JSON
        String event = data.getString("event");//防止event为空的异常
        if("start-matching".equals(event)){
            startMatching();
        }else if("stop-matching".equals(event)){
            stopMatching();
        }else if("move".equals(event)){
            System.out.println("move!!!!!!");
            Integer direction = data.getInteger("direction");
            System.out.println(direction);
            move(direction);
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

    private void move(Integer direction) {
        //判断是A玩家还是B玩家在操作
        if(game.getPlayerA().getId().equals(user.getId())){
            System.out.println("A在操作");
            game.setNextStepA(direction);
        }else if(game.getPlayerB().getId().equals(user.getId())){
            System.out.println("B在操作");
            game.setNextStepB(direction);
        }else{
            Exception e = new Exception("Error");
            e.printStackTrace();
        }
    }

}
