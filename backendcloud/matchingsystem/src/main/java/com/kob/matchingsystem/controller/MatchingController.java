package com.kob.matchingsystem.controller;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.kob.matchingsystem.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Objects;

@RestController
public class MatchingController {

    @Autowired
    private MatchingService matchingService;
    @PostMapping("/player/add/")
    public String addPlayer(@RequestParam MultiValueMap<String,String>data){
        int userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        int rating = Integer.parseInt(Objects.requireNonNull(data.getFirst("rating")));
        int botId = Integer.parseInt(Objects.requireNonNull(data.getFirst("bot_id")));
        return matchingService.addPlayer(userId,rating,botId);
    }

    @PostMapping("/player/remove/")
    public String removePlayer(@RequestParam MultiValueMap<String,String> data){
        int userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        return matchingService.removePLayer(userId);
    }
}
