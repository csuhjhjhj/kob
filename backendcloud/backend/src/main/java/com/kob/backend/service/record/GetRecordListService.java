package com.kob.backend.service.record;

import com.alibaba.fastjson.JSONObject;

public interface GetRecordListService {
    JSONObject getList(Integer page);//page表示页号 只需要返回对应页号的list
}
