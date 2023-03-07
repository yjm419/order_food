package com.yjm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Food {
    private int id;
    private int sId;
    private String title;
    private BigDecimal price;
    private BigDecimal priceOff;
    private String image;
    private String describes;

//    @TableField(exist = false)
//    private Shop_car shopCar;
    @TableField(exist = false)
    private int num ;
    @TableField(exist = false)
    private int aid;

}
