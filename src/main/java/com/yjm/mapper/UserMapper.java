package com.yjm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yjm.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("select id,name,phone,account from user where id=#{id}")
    User getUserPart(Integer id);
}
