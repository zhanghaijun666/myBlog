package com.blog.controller;

import com.blog.config.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/redis")
public class RedisController {

    private static final int REDIS_DE_DEMO = 10;

    @Autowired
    RedisUtil redisUtil;

    @GetMapping("getRedis")
    public ModelMap getRedis() {
        redisUtil.set("2020", "这是一条测试数据", REDIS_DE_DEMO);
        Long resExpire = redisUtil.expire("2020", 2, REDIS_DE_DEMO);//设置key过期时间
        System.out.println("resExpire=" + resExpire);


        ModelMap modelMap = new ModelMap();
        modelMap.put("status", 200);
        modelMap.put("data", redisUtil.get("2020", REDIS_DE_DEMO));
        return modelMap;
    }
}
