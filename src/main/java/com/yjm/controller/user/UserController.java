package com.yjm.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yjm.entity.OrderStatusNum;
import com.yjm.entity.StatusCount;
import com.yjm.entity.User;
import com.yjm.mapper.OrderMapper;
import com.yjm.service.OrderDetailesService;
import com.yjm.service.OrderService;
import com.yjm.service.UserService;
import com.yjm.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {
   ObjectMapper objectMapper= new ObjectMapper();
   @Autowired
   private UserService userService;
   @Autowired
   private OrderService orderService;
   @Autowired
   private OrderDetailesService orderDetailesService;
   @Autowired
   private RedisTemplate redisTemplate;
   @Autowired
   private OrderMapper orderMapper;


//登录检查
    @RequestMapping("/login")
    public String login(User user, HttpSession session) throws JsonProcessingException {
        String phone = user.getAccount().trim();
        String password = MD5Util.encode(user.getPassword().trim());
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("phone",phone);
        wrapper.or();
        wrapper.eq("account",user.getAccount().trim());

        User sqlUser = userService.getOne(wrapper);

        if (sqlUser.getLimits()==1){
            if (sqlUser.getPassword().equals(password)){
                session.setAttribute("user",sqlUser);
                String toHtmlURI = (String) session.getAttribute("toHtmlURI");
                if (toHtmlURI.equals("/user/userCenter")){
                    toHtmlURI="/view/myinfo";
                }
                return toHtmlURI;
            }
        }
        return "/view/tologin";
    }
//检查用户是否登录
    @RequestMapping("/islogin")
    public boolean islogin(HttpServletRequest request, @RequestParam(value = "url")String url){
        HttpSession session =request.getSession();
        Object user = session.getAttribute("user");
        String replace = url.replace("*", "&");
        session.setAttribute("toHtmlURI",replace);
        if (user!=null){
            return true;
        }else {
            return false;
        }
    }

    //注册
    @RequestMapping("/register")
    public boolean register(User user){
        String account = user.getAccount();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("account",account);
        User one = userService.getOne(wrapper);
        if (one==null){
            String password = user.getPassword();
            String encode = MD5Util.encode(password);
            user.setPassword(encode);
            user.setLimits(1);
            boolean save = userService.save(user);
            return  save;
        }
        return false;
    }

//个人中心信息获取
    @RequestMapping("/userCenter")
    public String userCenter(HttpServletRequest request) throws JsonProcessingException {
        HttpSession session = request.getSession();
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
        Integer commentCount = orderMapper.getCommentCount(user.getId());
        OrderStatusNum orderStatusNum = new OrderStatusNum(waitPay,waitDeliver,waitTack,commentCount);


        Map<String,Object> map = new HashMap<>();
        map.put("user",user);
        map.put("orderData",orderStatusNum);
        String jsonMap = objectMapper.writeValueAsString(map);

        return jsonMap;
    }


}
