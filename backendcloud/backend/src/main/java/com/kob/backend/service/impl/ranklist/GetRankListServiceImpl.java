package com.kob.backend.service.impl.ranklist;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.ranklist.GetRankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.tags.Param;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

@Service
public class GetRankListServiceImpl implements GetRankListService {

    @Autowired
    private UserMapper userMapper;
    @Override
    public JSONObject getList(Integer page) {
        IPage<User>userIPage = new Page<>(page,3);//展示page页，每页展示三个
        QueryWrapper<User>queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("rating");//按照rating降序排列
        List<User>users = userMapper.selectPage(userIPage,queryWrapper).getRecords();
        JSONObject resp = new JSONObject();
        for(User user : users){
            user.setPassword("");
        }
        resp.put("users",users);
        resp.put("user_count",userMapper.selectCount(null));
        return resp;
    }
}
