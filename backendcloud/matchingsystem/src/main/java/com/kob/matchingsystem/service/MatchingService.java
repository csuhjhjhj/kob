package com.kob.matchingsystem.service;

public interface MatchingService {
    String addPlayer (Integer userId,Integer rating);//匹配需要按照分值
    String removePLayer (Integer userId);
}
