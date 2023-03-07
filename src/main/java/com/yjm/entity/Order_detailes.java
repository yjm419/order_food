package com.yjm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order_detailes {
    private int id;
    private int oId;
    private int uId;
    private int sId;
    private int fId;
    private int num;
    private int isComment;
}
