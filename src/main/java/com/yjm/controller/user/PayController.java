package com.yjm.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yjm.entity.Address;
import com.yjm.entity.Food;
import com.yjm.entity.Orders;
import com.yjm.entity.User;
import com.yjm.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/pay")
public class PayController {
    ObjectMapper objectMapper =  new ObjectMapper();

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AddressService addressService;
//单个购买添加数据到redis --生成未支付订单
    @RequestMapping("/waitPay")
    public String waitPay(@RequestBody Food food,HttpSession session){
        Random random = new Random();
        String anInt = String.valueOf(random.nextInt(999999));
        User user = (User) session.getAttribute("user");
        String key = user.getName()+anInt;

        redisTemplate.opsForValue().set(key,food,15, TimeUnit.MINUTES);

        return key;
    }
    //单个购买获取redis数据
    @RequestMapping("/payfor")
    public String pay(@RequestParam(value = "key")String key,@RequestParam(value = "aid")Integer aid,HttpSession session) throws JsonProcessingException {
        QueryWrapper<Orders> wrapper2 = new QueryWrapper();
        wrapper2.eq("status",1);
        QueryWrapper<Orders> wrapper3 = new QueryWrapper();
        wrapper3.eq("status",2);

        Object food =  redisTemplate.opsForValue().get(key);
        Address address = addressService.getById(aid);



        Map<String,Object> map = new HashMap<>();
        map.put("food",food);
        map.put("address",address);

        String jsonMap = objectMapper.writeValueAsString(map);

        return jsonMap;
    }

//购物车购买添加到redis ----生成未支付订单
    @RequestMapping("/waitPays")
    public String waitPays(@RequestBody List<Food> food,HttpSession session){
        Random random = new Random();
        String anInt = String.valueOf(random.nextInt(999999));
        User user = (User) session.getAttribute("user");
        String key = user.getName()+anInt;

        redisTemplate.opsForValue().set(key,food,15, TimeUnit.MINUTES);

        return key;
    }

    //购物车购买获取redis数据
    @RequestMapping("/payfors")
    public String pays(@RequestParam(value = "key")String key,@RequestParam(value = "aid")Integer aid) throws JsonProcessingException {
        Object food =  redisTemplate.opsForValue().get(key);
        Address address = addressService.getById(aid);

        Map<String,Object> map = new HashMap<>();
        map.put("food",food);
        map.put("address",address);
        String jsonMap = objectMapper.writeValueAsString(map);

        return jsonMap;
    }
}
