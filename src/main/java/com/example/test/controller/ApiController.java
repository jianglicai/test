package com.example.test.controller;

import com.example.test.entity.SendEntity;
import com.example.test.entity.SendTgEntity;
import com.example.test.util.telegram.TelegramNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private TelegramNotifier notifier;

    @PostMapping(value = "/send")
    public Object send(@RequestBody SendEntity sendEntity) {
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        map.put("msg","操作成功！");
        map.put("data", sendEntity);
        System.out.println(sendEntity.getPhones()+"。发送内容："+sendEntity.getContent());
        return map;
    }

    @PostMapping(value = "/sendTg")
    public Object sendTg(@RequestBody SendTgEntity sendTgEntity) {
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        map.put("msg","操作成功！");
        map.put("data", sendTgEntity);
        String[] chatIds = sendTgEntity.getChatIds().split(",");
        for (String chatId : chatIds) {
            notifier.sendMessage(chatId,sendTgEntity.getContent());
        }
        return map;
    }

}
