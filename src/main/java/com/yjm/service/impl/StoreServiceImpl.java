package com.yjm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjm.entity.Store;
import com.yjm.mapper.StoreMapper;
import com.yjm.service.StoreService;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {
}
