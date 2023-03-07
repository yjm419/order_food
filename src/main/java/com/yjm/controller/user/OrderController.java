package com.yjm.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yjm.entity.*;
import com.yjm.mapper.OrderMapper;
import com.yjm.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    OrderMapper orderMapper;

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailesService orderDetailesService;
    @Autowired
    private RedisTemplate  redisTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private FoodService foodService;
    @Autowired
    private AddressService addressService;

    //单独购买生成订单
    @RequestMapping("/singleOrder")
    public boolean singleOrder(@RequestParam(value = "aId")Integer aId,
                               @RequestParam(value = "key")String key,
                               HttpSession session){
        User user = (User) session.getAttribute("user");
        int id = user.getId();
        Date date = new Date();

        Food food = (Food) redisTemplate.opsForValue().get(key);
        BigDecimal price = food.getPrice();
        BigDecimal num = BigDecimal.valueOf(food.getNum());
        BigDecimal totalMoney = num.multiply(price);

        Orders orders = new Orders(0,id,aId,date,totalMoney,key,1,0);
        boolean save1 = orderService.save(orders);
        int oId = 0;
        if (save1){
            QueryWrapper<Orders> wrapper = new QueryWrapper<>();
            wrapper.eq("number",key);
            Orders one = orderService.getOne(wrapper);
            oId = one.getId();
        }
        Order_detailes orderDetailes = new Order_detailes(0,oId,id,food.getSId(),food.getId(),food.getNum(),0);
        boolean save2 = orderDetailesService.save(orderDetailes);
        Boolean delete= false;
        if (save1 && save2) {
            delete = redisTemplate.delete(key);
        }

        return delete;
    }
    //购物车购买支付
    @RequestMapping("/multipleorder")
    public boolean multipleorder(@RequestParam(value = "aId")Integer aId,
                                @RequestParam(value = "key")String key,
                                HttpSession session){
        User user = (User) session.getAttribute("user");
        int id = user.getId();
        Date date = new Date();
        BigDecimal totalMoney =  BigDecimal.valueOf(0);

        List<Food> foodList = (List<Food>) redisTemplate.opsForValue().get(key);

        for (int i = 0; i < foodList.size(); i++) {
            BigDecimal price = foodList.get(i).getPrice();
            BigDecimal  num = BigDecimal.valueOf(foodList.get(i).getNum());
            totalMoney =totalMoney.add(num.multiply(price)) ;

        }
        Orders orders = new Orders(0,id,aId,date,totalMoney,key,1,0);
        boolean save1 = orderService.save(orders);

        int oId = 0;
        boolean save2 =false;
        if (save1){
            QueryWrapper<Orders> wrapper = new QueryWrapper<>();
            wrapper.eq("number",key);
            Orders one = orderService.getOne(wrapper);
            oId = one.getId();
            for (int i = 0; i < foodList.size(); i++) {
                Order_detailes orderDetailes = new Order_detailes(0,oId,id,foodList.get(i).getSId(),foodList.get(i).getId(),foodList.get(i).getNum(),0);
                save2 =  orderDetailesService.save(orderDetailes);
            }
        }
        Boolean delete= false;
        if (save1 && save2) {
            delete = redisTemplate.delete(key);
        }

        return delete;
    }
//待支付订单信息
    @RequestMapping("/orderwaitpay")
    public String orderwaitpay(HttpSession session) throws JsonProcessingException {
        User sessionUser = (User) session.getAttribute("user");
        int id = sessionUser.getId();
        User user = userService.getById(id);

        String key = user.getName()+"*";
        Set keys = redisTemplate.keys(key);
        int waitPay = keys.size();//待付款数目
        List<StatusCount> statusCount = orderService.getStatusCount(user.getId());
        int waitDeliver = 0;
        int waitTack = 0;
        for (int i = 0; i < statusCount.size(); i++) {
            if (statusCount.get(i).getStatus()==0){
                waitDeliver = statusCount.get(i).getNum();
            }
            if (statusCount.get(i).getStatus()==1){
                waitTack = statusCount.get(i).getNum();
            }
        }
        Integer commentCount = orderService.getCommentCount(user.getId());
        OrderStatusNum orderStatusNum = new OrderStatusNum(waitPay,waitDeliver,waitTack,commentCount);

         List<Object> objectList = new ArrayList<>();
        for (Object k : keys) {
            Object o = redisTemplate.opsForValue().get(k);
             objectList.add(o);
        }


        Map<String,Object> map = new HashMap<>();
        map.put("orderData",orderStatusNum);
        map.put("food",objectList);
        map.put("key",keys);

        String jsonMap = objectMapper.writeValueAsString(map);
        return jsonMap;
    }
    //删除待付款订单
    @RequestMapping("/deleteWaitOrder")
    public boolean deleteWaitOrder(@RequestParam(value = "key")String key){
        Boolean delete = redisTemplate.delete(key);
        return delete;
    }
    //获取待发货信息
    @RequestMapping("/orderwaitdeliver")
    public String orderwaitdeliver(HttpSession session) throws JsonProcessingException {
        User user = (User) session.getAttribute("user");
        //查询所有未发货的订单
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        Map<String,Object> wrappermap = new HashMap<>();
        wrappermap.put("u_id",user.getId());
        wrappermap.put("status",0);
        wrapper.allEq(wrappermap);
        List<Orders> ordersList = orderService.list(wrapper);//111
        List<Food> foodList = orderService.findFoodByDetailes(user.getId(), 0);
        List<Order_detailes> orderDetailesList = orderService.findDetaliesByStatus(user.getId(), 0);

        String key = user.getName()+"*";
        Set keys = redisTemplate.keys(key);
        int waitPay = keys.size();//待付款数目
        List<StatusCount> statusCount = orderService.getStatusCount(user.getId());
        int waitDeliver = 0;
        int waitTack = 0;
        for (int i = 0; i < statusCount.size(); i++) {
            if (statusCount.get(i).getStatus()==0){
                waitDeliver = statusCount.get(i).getNum();
            }
            if (statusCount.get(i).getStatus()==1){
                waitTack = statusCount.get(i).getNum();
            }
        }
        Integer commentCount = orderService.getCommentCount(user.getId());
        OrderStatusNum orderStatusNum = new OrderStatusNum(waitPay,waitDeliver,waitTack,commentCount);

        Map<String,Object> map = new HashMap<>();
        map.put("orderData",orderStatusNum);//111
        map.put("food",foodList);//111
        map.put("orders",ordersList);//1111
        map.put("orderdetailes",orderDetailesList);
        String jsonMap = objectMapper.writeValueAsString(map);
        return jsonMap;
    }

    //获取待收货货信息
    @RequestMapping("/orderwaittake")
    public String orderwaittake(HttpSession session) throws JsonProcessingException {
        User user = (User) session.getAttribute("user");
        //查询所有未发货的订单
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        Map<String,Object> wrappermap = new HashMap<>();
        wrappermap.put("u_id",user.getId());
        wrappermap.put("status",1);
        wrapper.allEq(wrappermap);
        List<Orders> ordersList = orderService.list(wrapper);
        List<Food> foodList = orderService.findFoodByDetailes(user.getId(), 1);
        List<Order_detailes> orderDetailesList = orderService.findDetaliesByStatus(user.getId(), 1);

        String key = user.getName()+"*";
        Set keys = redisTemplate.keys(key);
        int waitPay = keys.size();//待付款数目
        List<StatusCount> statusCount = orderService.getStatusCount(user.getId());
        int waitDeliver = 0;
        int waitTack = 0;
        for (int i = 0; i < statusCount.size(); i++) {
            if (statusCount.get(i).getStatus()==0){
                waitDeliver = statusCount.get(i).getNum();
            }
            if (statusCount.get(i).getStatus()==1){
                waitTack = statusCount.get(i).getNum();
            }
        }
        Integer commentCount = orderService.getCommentCount(user.getId());
        OrderStatusNum orderStatusNum = new OrderStatusNum(waitPay,waitDeliver,waitTack,commentCount);

        Map<String,Object> map = new HashMap<>();
        map.put("orderData",orderStatusNum);
        map.put("food",foodList);
        map.put("orders",ordersList);
        map.put("orderdetailes",orderDetailesList);
       // map.put("address",addressList);
        String jsonMap = objectMapper.writeValueAsString(map);
        return jsonMap;
    }
    //确认收货
    @RequestMapping("/confirmorder")
    public boolean confirmorder(@RequestParam("oid")Integer oid){
        UpdateWrapper<Orders> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",oid);
        updateWrapper.set("status",2);
        boolean update = orderService.update(updateWrapper);
        return update;
    }
    //获取待评价信息
    @RequestMapping("/orderwaitcomment")
    public String orderwaitcomment(HttpSession session) throws JsonProcessingException {
        User user = (User) session.getAttribute("user");
        int id = user.getId();

        List<Orders> ordersList = orderService.findOrdersByDetailes(id);
        List<Food> foodList = orderService.findFoodByDetailes(id,2);
        List<Order_detailes> orderDetailesList = orderService.findDetaliesByStatus(id,2);


        //各种状态数量
        String key = user.getName()+"*";
        Set keys = redisTemplate.keys(key);
        int waitPay = keys.size();//待付款数目
        List<StatusCount> statusCount = orderService.getStatusCount(user.getId());
        int waitDeliver = 0;
        int waitTack = 0;
        for (int i = 0; i < statusCount.size(); i++) {
            if (statusCount.get(i).getStatus()==0){
                waitDeliver = statusCount.get(i).getNum();
            }
            if (statusCount.get(i).getStatus()==1){
                waitTack = statusCount.get(i).getNum();
            }
        }
        Integer commentCount = orderService.getCommentCount(id);
        OrderStatusNum orderStatusNum = new OrderStatusNum(waitPay,waitDeliver,waitTack,commentCount);


        Map<String,Object> map = new HashMap<>();
        map.put("food",foodList);
        map.put("orders",ordersList);
        map.put("orderdetailes",orderDetailesList);
        map.put("orderData",orderStatusNum);
        String jsonMap = objectMapper.writeValueAsString(map);
        return jsonMap;
    }

    //非待支付订单详情
    @RequestMapping("/orderdetaile")
    public String orderdetaile(@RequestBody Order_detailes orderDetailes) throws JsonProcessingException {
        //查询订单
        Orders orders = orderService.getById(orderDetailes.getOId());

        //获得商品信息
        QueryWrapper<Order_detailes> orderDetailesQueryWrapper = new QueryWrapper<>();
        orderDetailesQueryWrapper.eq("o_id",orderDetailes.getOId());
        List<Order_detailes> list = orderDetailesService.list(orderDetailesQueryWrapper);
        List<Food> foodList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Food food = foodService.getById(list.get(i).getFId());
            food.setNum(list.get(i).getNum());
            foodList.add(food);
        }
        //获取地址信息
        Address address = addressService.getById(orders.getAddrId());

        Map<String,Object> map = new HashMap<>();

//        map.put("orderdetailes",orderDetailesList);
        map.put("food",foodList);
        map.put("orders",orders);
        map.put("address",address);
        String jsonMap = objectMapper.writeValueAsString(map);
        return jsonMap;
    }

    //待支付订单详情
    @RequestMapping("/aorderdetaile")
    public String aorderdetaile(@RequestParam(value = "key")String key,@RequestParam(value = "aid")Integer aid) throws JsonProcessingException {
        System.out.println(key);
        System.out.println(aid);
        Object o = redisTemplate.opsForValue().get(key);
        Address address = addressService.getById(aid);

        Map<String,Object> map = new HashMap<>();
        map.put("food",o);
        map.put("address",address);
        String jsonMap = objectMapper.writeValueAsString(map);
        return jsonMap;
    }
}
