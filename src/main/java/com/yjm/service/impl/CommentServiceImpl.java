package com.yjm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yjm.entity.Comments;
import com.yjm.mapper.CommentMapper;
import com.yjm.service.CommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comments> implements CommentService {
}
