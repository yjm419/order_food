package com.yjm.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yjm.entity.Address;
import com.yjm.entity.Food;
import com.yjm.entity.Shop_car;
import com.yjm.entity.User;
import com.yjm.service.AddressService;
import com.yjm.service.FoodService;
import com.yjm.service.ShopCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/submint")
public class SubmitController {
    ObjectMapper objectMapper =  new ObjectMapper();
    @Autowired
    private AddressService addressService;
    @Autowired
    private FoodService foodService;
    @Autowired
    private ShopCarService shopCarService;


    @RequestMapping("/info")
    public String getInfo(@RequestParam(value = "fId")Integer fId, HttpSession session) throws JsonProcessingException {
        Food food = foodService.getById(fId);
        food.setNum(1);
        User user = (User) session.getAttribute("user");
        int id = user.getId();
        QueryWrapper<Address> wrapper = new QueryWrapper<>();
        wrapper.eq("u_id",id);
        wrapper.eq("is_default",1);

        Address address = addressService.getOne(wrapper);

        Map<String,Object> map = new HashMap<>();
        map.put("food",food);
        map.put("address",address);
        String jsonMap = objectMapper.writeValueAsString(map);

        return jsonMap;
    }
    @RequestMapping("/infos")
    public String infos(HttpSession session,@RequestBody Integer[] shopCartScIds ) throws JsonProcessingException {
        User user = (User) session.getAttribute("user");
        int id = user.getId();

        QueryWrapper<Address> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("u_id",id);
        wrapper2.eq("is_default",1);

        List<Food> foodList = new ArrayList<>();
        for (int i = 0; i < shopCartScIds.length; i++) {
            Shop_car shopCar = shopCarService.getById(shopCartScIds[i]);
            Food food = foodService.getById(shopCar.getFId());
            food.setNum(shopCar.getNum());

            foodList.add(food);
        }

        Address address = addressService.getOne(wrapper2);

        Map<String,Object> map = new HashMap<>();
        map.put("address",address);
        map.put("food",foodList);

        String jsonMap = objectMapper.writeValueAsString(map);
        return jsonMap;
    }

}
