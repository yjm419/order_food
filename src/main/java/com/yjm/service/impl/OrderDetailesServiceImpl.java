package com.yjm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjm.entity.Order_detailes;
import com.yjm.mapper.OrderDetailesMapper;
import com.yjm.service.OrderDetailesService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailesServiceImpl extends ServiceImpl<OrderDetailesMapper, Order_detailes> implements OrderDetailesService {
}
