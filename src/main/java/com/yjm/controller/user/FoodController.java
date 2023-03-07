package com.yjm.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yjm.entity.Comments;
import com.yjm.entity.Food;
import com.yjm.entity.Store_img;
import com.yjm.entity.User;
import com.yjm.mapper.UserMapper;
import com.yjm.service.CommentService;
import com.yjm.service.FoodService;
import com.yjm.service.StoreimgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/food")
public class FoodController {
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private FoodService foodService;
    @Autowired
    private StoreimgService storeimgService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserMapper userMapper;
    //店铺中商品的搜索
    @RequestMapping("/getFoodByTitle")
    public String getFoodByTitle(@RequestParam(value = "sId")Integer sId, @RequestParam(value = "title")String title) throws JsonProcessingException {
        QueryWrapper<Food> wrapper = new QueryWrapper<>();
        wrapper.like("title",title);
        wrapper.eq("s_id",sId);

        List<Food> foodList = foodService.list(wrapper);
        Map<String,Object> map = new HashMap<>();
        map.put("food",foodList);
        String jsonMap = objectMapper.writeValueAsString(map);
        return jsonMap;
    }

    //获取店铺中的商品
    @RequestMapping("/getFoodInStore")
    public String getStoreDel(@RequestParam(value = "sId")Integer sId) throws JsonProcessingException {
        QueryWrapper wrapper = new QueryWrapper<>();
        wrapper.eq("s_id",sId);

        List<Food> foodlist = foodService.list(wrapper);
        List<Store_img> storeimglist = storeimgService.list(wrapper);
        Map<String,Object> map = new HashMap<>();
        map.put("food",foodlist);
        map.put("storeimg",storeimglist);
        String jsonMap = objectMapper.writeValueAsString(map);

        return  jsonMap;

    }

    //店铺中点击商品进入商品详情
    @RequestMapping("/getFoodByid")
    public String getFoodByid(@RequestParam(value = "id")Integer id,@RequestParam(value = "sId")Integer sId) throws JsonProcessingException {
        QueryWrapper<Comments> wrapper = new QueryWrapper<>();
        Map<String,Object> map1 = new HashMap<>();
        map1.put("f_id",id);
        map1.put("s_id",sId);
        wrapper.allEq(map1);

        Food food = foodService.getById(id);
        List<Comments> commentsList = commentService.list(wrapper);

        for (int i = 0; i < commentsList.size(); i++) {
            User userPart = userMapper.getUserPart(commentsList.get(i).getUId());
            commentsList.get(i).setUser(userPart);
        }

        Map<String,Object> map = new HashMap<>();
        map.put("food",food);
        map.put("comment", commentsList);
        String jsonMap = objectMapper.writeValueAsString(map);
        return jsonMap;
    }
}
