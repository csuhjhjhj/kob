package com.kob.backend.service.user.bot;

import com.kob.backend.pojo.Bot;

import java.util.List;

public interface GetListService {
    //查询当前用户的bot
    List<Bot>getlist();
    //对于当前用户的user id保存在token中，因此不用传参数
}
