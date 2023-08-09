package com.kob.backend.controller.user.bot;

import com.kob.backend.service.user.bot.AddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddController {
    @Autowired
    private AddService addService;//将AddService接口注入
    @PostMapping("/user/bot/add/")//处理涉及修改数据库的请求，用post
    public Map<String, String> add (@RequestParam Map<String,String> data){
        return addService.add(data);
    }
}
