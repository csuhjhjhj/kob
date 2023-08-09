package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Service
public class UpdateServiceImpl implements UpdateService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> update(Map<String, String> data) {
        //1.获取当前User
        UsernamePasswordAuthenticationToken authenticationToken
                = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser
                = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        //2.获取属性并检查
        String title = data.get("title");
        String description = data.get("description");
        String content = data.get("content");
        Date now = new Date();
        //语法检查
        Map<String, String> map = new HashMap<>();
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
        //3.获取bot并更新
        int bot_id = Integer.parseInt(data.get("bot_id"));
        Bot bot = botMapper.selectById(bot_id);
        if(bot == null){
            map.put("error_message","Bot不存在或已删除");
            return map;
        }
        if(!bot.getUserId().equals(user.getId())){
            map.put("error_message","没有权限修改该Bot");
            return map;
        }
        //注意 id userid不可改 rating不在此处修改
        try{
            bot.setTitle(title);
            bot.setDescription(description);
            bot.setContent(content);
            bot.setModifytime(now);
        }catch(Exception e) {
            System.out.println("更新失败");
        }
        botMapper.updateById(bot);
        map.put("error_message", "success");
        return map;
    }
}
