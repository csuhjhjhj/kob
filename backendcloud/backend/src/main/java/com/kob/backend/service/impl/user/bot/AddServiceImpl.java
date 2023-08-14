package com.kob.backend.service.impl.user.bot;

import com.kob.backend.config.SecurityConfig;
import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AddServiceImpl implements AddService {

    @Autowired
    private BotMapper botMapper;//注入接口
    @Override
    public Map<String, String> add(Map<String, String> data) {
        //1.获取user
        //同InfoServiceImpl一样，如果授权成功，则从上下文中将User信息提取出来
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        //2.获取data内容
        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");

        Map<String,String>map = new HashMap<>();//定义return 格式
        //语法检查
        if(title == null || title.length() == 0){
            map.put("error_message","标题不能为空");
            return map;
        }
        if(title.length() > 100){
            map.put("error_message","标题长度不能大于100");
            return map;
        }
        if(description == null || description.length() == 0){
            description = "这个用户很懒，什么也没有留下";
        }
        if(description.length() > 300){
            map.put("error_message","Bot描述的长度不能大于300");
            return map;
        }
        if(content == null || content.length() == 0){
            map.put("error_message","代码不能为空");
            return map;
        }
        if(content.length() > 10000){
            map.put("error_message","代码长度不能大于10000");
            return map;
        }
        //3.创建Bot对象
        Date now = new Date();
        //id自增 因此不用传
        Bot bot = new Bot(null,user.getId(),title,description,content,now,now);
        botMapper.insert(bot);
        map.put("error_message","success");
        return map;
    }
}
