package com.yjm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjm.entity.Food;
import com.yjm.entity.Order_detailes;
import com.yjm.entity.Orders;
import com.yjm.entity.StatusCount;
import com.yjm.mapper.OrderMapper;
import com.yjm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {
    @Autowired
    private  OrderMapper orderMapper;

    @Override
    public List<Orders> findOrdersByDetailes(Integer id) {

        return orderMapper.findOrdersByDetailes(id);
    }

    @Override
    public List<Order_detailes> findDetaliesByStatus(Integer id,Integer status) {
        return orderMapper.findDetaliesByStatus(id,status);
    }

    @Override
    public List<Food> findFoodByDetailes(Integer id,Integer status) {
        return orderMapper.findFoodByDetailes(id,status);
    }

    @Override
    public Integer getCommentCount(Integer id) {
        return orderMapper.getCommentCount(id);
    }

    @Override
    public List<StatusCount> getStatusCount(Integer id) {
        return orderMapper.getStatusCount(id);
    }
}
