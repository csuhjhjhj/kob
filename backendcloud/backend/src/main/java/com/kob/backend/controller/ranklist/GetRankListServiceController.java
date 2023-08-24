package com.kob.backend.controller.ranklist;


import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.ranklist.GetRankListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetRankListServiceController {
    @Autowired
    private GetRankListService getRankListService;

    @GetMapping("/ranklist/getlist/")
    public JSONObject getlist(@RequestParam Map<String,String>data){
        Integer page = Integer.parseInt(data.get("page"));
        System.out.println(page);
        return getRankListService.getList(page);
    }
}
