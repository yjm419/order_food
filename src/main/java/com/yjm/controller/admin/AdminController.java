package com.yjm.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yjm.entity.Address;
import com.yjm.entity.Admin;
import com.yjm.entity.Orders;
import com.yjm.entity.User;
import com.yjm.service.AddressService;
import com.yjm.service.AdminService;
import com.yjm.service.OrderService;
import com.yjm.service.UserService;
import com.yjm.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AddressService addressService;
    @RequestMapping("/adminLogin")
    public boolean adminLogin(Admin admin, HttpSession session){
        String phone = admin.getAccount().trim();
        String password = MD5Util.encode(admin.getPassword().trim());
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("phone",phone);
        wrapper.or();
        wrapper.eq("account",admin.getAccount().trim());
        Admin sqlAdmin = adminService.getOne(wrapper);

        if (sqlAdmin.getLimits()==1){
            if (sqlAdmin.getPassword().equals(password)){
                session.setAttribute("admin",sqlAdmin);
               return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    @RequestMapping("/getUserInfo")
    public String getUserInfo() throws JsonProcessingException {
        List<User> userList = userService.list();

        Map<String,Object> map = new HashMap<>();
        map.put("user",userList);
        String jsonMap = objectMapper.writeValueAsString(map);
        return jsonMap;
    }

    @RequestMapping("/getUserInfoById")
    public String getUserInfoById(@RequestParam(value = "id")Integer id,HttpSession session) throws JsonProcessingException {
        User user = userService.getById(id);
        session.setAttribute("user",user);
        //session.setAttribute("user",user);
        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.eq("u_id",id);
        List<Orders> ordersList = orderService.list(wrapper);
//        Page<Orders> ordersPage = new Page<>(pn,6);
//        Page<Orders> page = orderService.page(ordersPage, wrapper);

        QueryWrapper<Address> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("u_id",id);
        List<Address> addressList = addressService.list(wrapper1);

        Map<String,Object> map = new HashMap<>();
        map.put("user",user);
        map.put("orders",ordersList);
        map.put("address",addressList);
        String jsonMap = objectMapper.writeValueAsString(map);
        return jsonMap;
    }

    @RequestMapping("/addressInfo")
    public String getAddress(@RequestParam(defaultValue = "aid")Integer aid) throws JsonProcessingException {
        Address address = addressService.getById(aid);
        Map<String,Object> map = new HashMap<>();
        map.put("address",address);
        String jsonMap = objectMapper.writeValueAsString(map);
        return jsonMap;
    }

    //确认发货
    @RequestMapping("/confirmDeliver")
    public boolean confirmDeliver(@RequestParam("oid")Integer oid){
        UpdateWrapper<Orders> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",oid);
        updateWrapper.set("status",1);
        boolean update = orderService.update(updateWrapper);
        return update;
    }

    @RequestMapping("/disabled")
    public boolean disabled(@RequestParam(defaultValue = "id")Integer id,@RequestParam(defaultValue = "status")Integer status){
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id);
        updateWrapper.set("limits",status);
        boolean update = userService.update(updateWrapper);
        return update;
    }

}
