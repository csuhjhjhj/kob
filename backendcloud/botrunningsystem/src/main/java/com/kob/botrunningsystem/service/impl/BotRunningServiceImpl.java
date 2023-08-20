package com.kob.botrunningsystem.service.impl;

import com.kob.botrunningsystem.service.BotRunningService;
import com.kob.botrunningsystem.service.impl.utils.BotPool;
import org.springframework.stereotype.Service;

@Service
public class BotRunningServiceImpl implements BotRunningService {

    public final static BotPool botPool = new BotPool();//存储BotRuning线程
    @Override
    public String addBot(Integer userId, String botcode, String input) {
//        System.out.println("add bot "+ userId + " " + botcode + " " + input);
        botPool.addBot(userId,botcode,input);
        return "add bot success";
    }
}
