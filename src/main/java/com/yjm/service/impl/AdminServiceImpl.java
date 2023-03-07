package com.yjm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjm.entity.Admin;
import com.yjm.mapper.AdminMapper;
import com.yjm.service.AdminService;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
}
