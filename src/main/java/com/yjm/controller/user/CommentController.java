package com.yjm.controller.user;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yjm.entity.Comments;
import com.yjm.entity.Order_detailes;
import com.yjm.entity.User;
import com.yjm.service.CommentService;
import com.yjm.service.OrderDetailesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
@RequestMapping("/comm")
public class CommentController {
    ObjectMapper objectMapper =  new ObjectMapper();
    @Autowired
    private CommentService commentService;
    @Autowired
    private  OrderDetailesService orderDetailesService;
    //评论
    @RequestMapping("comment")
    public boolean getInfo(@RequestParam(value = "content")String content,
                          @RequestParam(value = "odid")Integer odid,
                          HttpSession session){

        User user = (User) session.getAttribute("user");
        Order_detailes orderDetailes = orderDetailesService.getById(odid);

        Comments comments = new Comments();
        comments.setId(0);
        comments.setUId(user.getId());
        comments.setSId(orderDetailes.getSId());
        comments.setFId(orderDetailes.getFId());
        comments.setContent(content);
        comments.setTime(new Date());
        boolean b = commentService.save(comments);

        UpdateWrapper<Order_detailes> orderDetailesUpdateWrapper = new UpdateWrapper<>();
        orderDetailesUpdateWrapper.eq("id",odid);
        orderDetailesUpdateWrapper.set("is_comment",1);
        boolean update = false;
        if (b){
             update = orderDetailesService.update(orderDetailesUpdateWrapper);
        }

        return update;
    }
}
