package com.example.test.controller;

import com.example.test.util.test.GetListIpSets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/test")
public class TestController {

    @Value(value = "${aws.waf.region}")
    private String awsWafRegion;

    @Value("${mpw.key}")
    private String mpwKey;

    @GetMapping(value = "/getConfig")
    public Object getConfig() {
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        map.put("msg","操作成功！");
        map.put("data", awsWafRegion);
        map.put("mpwKey", mpwKey);
        return map;
    }
    @PostMapping(value = "/test1")
    public Object postHtmlParams(@RequestBody String body) {
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        map.put("msg","操作成功！");
        map.put("data", body);
        return map;
    }

    @GetMapping("/getListIpSets")
    public Object GetListIpSets() {
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        map.put("msg","操作成功！");
        map.put("data", GetListIpSets.getListIpSets());
        return map;
    }

    @GetMapping("/main")
    public Object list() {
        Map<String,Object> map = new HashMap<>();
        map.put("success",true);
        map.put("msg","操作成功！");
        return map;
    }

    @GetMapping("/main1")
    public void main1() {
        int test=1;
        while (1==1) {
            // 检查某个条件，如果满足则退出循环
            ++test;
            --test;
            // 循环体
        }
    }

    List<byte[]> memoryList = new ArrayList<>();

    @GetMapping("/memoryTest")
    public String memoryTest(int c) {
        byte[] b = new byte[c * 1024 * 1024];
        memoryList.add(b);
        return "success";
    }

    ThreadPoolExecutor executor = new ThreadPoolExecutor(
            10,
            15,
            2,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(50),
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    @GetMapping("/threadLock")
    public String threadLock() {
        Object resourceA = new Object();
        Object resourceB = new Object();
        executor.submit(() -> {
            synchronized (resourceA) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (resourceB) {
                }
            }
        });
        executor.submit(() -> {
            synchronized (resourceB) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (resourceA) {
                }
            }
        });
        return "success";
    }

}
