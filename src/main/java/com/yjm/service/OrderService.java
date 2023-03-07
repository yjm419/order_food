package com.yjm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yjm.entity.Food;
import com.yjm.entity.Order_detailes;
import com.yjm.entity.Orders;
import com.yjm.entity.StatusCount;

import java.util.List;

public interface OrderService extends IService<Orders> {
    List<Orders> findOrdersByDetailes(Integer id);
    List<Order_detailes> findDetaliesByStatus(Integer id,Integer status);
    List<Food> findFoodByDetailes(Integer id,Integer status);
    Integer getCommentCount(Integer id);
    List<StatusCount> getStatusCount(Integer id);
}
