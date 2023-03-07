package com.yjm.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yjm.entity.Food;
import com.yjm.entity.Shop_car;
import com.yjm.entity.User;
import com.yjm.service.FoodService;
import com.yjm.service.ShopCarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class ShopCarController {
    ObjectMapper objectMapper= new ObjectMapper();
    @Autowired
    private ShopCarService shopCarService;
    @Autowired
    private FoodService foodService;
    @RequestMapping("/shopcar")
    public String shopCar(HttpServletRequest request) throws JsonProcessingException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int id = user.getId();

        QueryWrapper<Shop_car> wrapper = new QueryWrapper();
        wrapper.eq("u_id",id);
        List<Shop_car> shop_carList = shopCarService.list(wrapper);
        for (int i = 0; i < shop_carList.size(); i++) {
            Food food = foodService.getById(shop_carList.get(i).getFId());
            shop_carList.get(i).setFood(food);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("carts",shop_carList);

        String jsonMap = objectMapper.writeValueAsString(map);

        return jsonMap;
    }
    //商品数目加一
    @RequestMapping("/add")
    public boolean add(@RequestParam(value = "id")Integer id){
        Shop_car shop_car = shopCarService.getById(id);
        int num = shop_car.getNum();
        UpdateWrapper<Shop_car> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",id);
        wrapper.set("num",num+1);
        boolean b = shopCarService.update(wrapper);
        return b;
    }
    //商品数目减一
    @RequestMapping("/reduce")
    public boolean reduce(@RequestParam(value = "id")Integer id){
        Shop_car shop_car = shopCarService.getById(id);
        int num = shop_car.getNum();
        UpdateWrapper<Shop_car> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",id);
        wrapper.set("num",num-1);
        boolean b = shopCarService.update(wrapper);
        return b;
    }
    //删除购物车商品
    @DeleteMapping
    public boolean deleteItem(@RequestBody Integer[] ids){
        boolean b = false;
        for (Integer id : ids) {
             b = shopCarService.removeById(id);
        }
        return b;
    }
//添加物品到购物车
    @RequestMapping("/addCart")
    public String addCart(HttpServletRequest request,
                          @RequestParam(value = "fId")Integer fId,
                          @RequestParam(value = "sId")Integer sId) throws JsonProcessingException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int uId = user.getId();
        Shop_car shop_car = new Shop_car(0,uId,sId,fId,1,null);

        QueryWrapper<Shop_car> wrapper = new QueryWrapper<>();
        Map<String,Object> map = new HashMap<>();
        map.put("u_id",uId);
        map.put("s_id",sId);
        map.put("f_id",fId);
        wrapper.allEq(map);
        Shop_car shopCar = shopCarService.getOne(wrapper);

        boolean b = false;
        String res = "";
        if (shopCar!=null){
            UpdateWrapper<Shop_car> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id",shopCar.getId());
            updateWrapper.set("num",shopCar.getNum()+1);
             b = shopCarService.update(updateWrapper);
        }else {
             b = shopCarService.save(shop_car);
        }

        if (b){
            res = objectMapper.writeValueAsString("添加成功");
        }else {
            res = objectMapper.writeValueAsString("添加失败");
        }
        return res;
    }
}
