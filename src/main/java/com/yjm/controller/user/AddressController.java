package com.yjm.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yjm.entity.Address;
import com.yjm.entity.User;
import com.yjm.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/address")
public class AddressController {
    ObjectMapper objectMapper =  new ObjectMapper();
    @Autowired
    private AddressService addressService;
//获取用户所有地址列表
    @RequestMapping("/getaddr")
    public String getaddr(HttpSession session) throws JsonProcessingException {
        User user = (User) session.getAttribute("user");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("u_id",user.getId());
        List<Address> addressList = addressService.list(wrapper);

        Map<String,Object> map = new HashMap<>();
        map.put("address",addressList);

        String jsonMap = objectMapper.writeValueAsString(map);
        return jsonMap;
    }
    //添加地址
    @RequestMapping("/add")
    public boolean add( @RequestBody Address address,HttpSession session){
        User user = (User) session.getAttribute("user");
        address.setUId(user.getId());

        boolean b = addressService.save(address);
        return b;
    }
    //修改地址
    @RequestMapping("/update")
    public boolean update( @RequestBody Address address){
        boolean b = addressService.updateById(address);
        return b;
    }
    //默认地址
    @RequestMapping("/default")
    public boolean defaultAddress(@RequestParam(value =  "id")Integer id){
        UpdateWrapper<Address> updateWrapper1 = new UpdateWrapper<>();
        UpdateWrapper<Address> updateWrapper2 = new UpdateWrapper<>();
        updateWrapper1.set("is_default",0);

        updateWrapper2.eq("id",id);
        updateWrapper2.set("is_default",1);

        boolean b = addressService.update(updateWrapper1);
        boolean update = false;
        if (b) {
            update = addressService.update(updateWrapper2);
        }
        return update;
    }
    //删除地址
    @RequestMapping("/delete")
    public boolean delete(@RequestParam(value = "id")Integer id){
        boolean b = addressService.removeById(id);
        return b;
    }
}
