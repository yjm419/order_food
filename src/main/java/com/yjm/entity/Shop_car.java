package com.yjm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Shop_car {
    private int id;
    private int uId;
    private int sId;
    private int fId;
    private int num;

    @TableField(exist = false)
    private Food food;


}
