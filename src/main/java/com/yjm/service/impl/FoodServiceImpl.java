package com.yjm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjm.entity.Food;
import com.yjm.mapper.FoodMapper;
import com.yjm.service.FoodService;
import org.springframework.stereotype.Service;

@Service
public class FoodServiceImpl extends ServiceImpl<FoodMapper, Food> implements FoodService {
}
