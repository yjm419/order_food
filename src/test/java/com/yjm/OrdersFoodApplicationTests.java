package com.yjm;

import com.baomidou.mybatisplus.core.toolkit.AES;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yjm.entity.*;
import com.yjm.mapper.OrderMapper;
import com.yjm.service.FoodService;
import com.yjm.service.OrderService;
import com.yjm.util.MD5Util;
import org.apache.tomcat.util.security.MD5Encoder;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class OrdersFoodApplicationTests {
    @Autowired
    StringEncryptor stringEncryptor;

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderService orderService;
    @Autowired
    private FoodService foodService;

    @Test
    void contextLoads() {
        String encode = MD5Util.encode("我是你爸爸！");

        System.out.println(encode);
    }

    @Test
    void  myPassword(){
        List<Food> foodByDetailes = orderService.findFoodByDetailes(1,1);
        System.out.println(foodByDetailes);
    }

    @Test
    void redistest(){
        List<StatusCount> statusCount = orderService.getStatusCount(2);
        int waitDeliver = 0;
        int waitTack = 0;
        for (int i = 0; i < statusCount.size(); i++) {
            if (statusCount.get(i).getStatus()==0){
                waitDeliver = statusCount.get(i).getNum();
            }
            if (statusCount.get(i).getStatus()==1){
                waitDeliver = statusCount.get(i).getNum();
            }
        }
        System.out.println("waitDeliver:"+waitDeliver);
        System.out.println("waitTack:"+waitTack);
    }
}
