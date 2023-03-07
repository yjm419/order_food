package com.yjm.controller.user;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yjm.entity.Store;
import com.yjm.service.FoodService;
import com.yjm.service.StoreService;
import com.yjm.service.StoreimgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/store")
public class StoreController {
    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private  StoreService storeService;
    @Autowired
    private FoodService foodService;
    @Autowired
    private StoreimgService storeimgService;

    //获取所有的店铺
    @CrossOrigin
    @RequestMapping("/allstore")
    public String getAllstudent() throws JsonProcessingException {
        List<Store> list = storeService.list();

        Map<String,Object> map = new HashMap<>();
        map.put("store",list);
        String jsonMap = objectMapper.writeValueAsString(map);

        return jsonMap;
    }

    //搜索店铺
    @RequestMapping("/getStoreByName")
    public String  getStoreByName(@RequestParam(value = "name")String name) throws JsonProcessingException {
        QueryWrapper<Store> wrapper = new QueryWrapper<>();
        wrapper.like("name",name);

        List<Store> list = storeService.list(wrapper);

        Map<String,Object> map = new HashMap<>();
        map.put("store",list);
        String jsonMap = objectMapper.writeValueAsString(map);

        return jsonMap;
    }


}
