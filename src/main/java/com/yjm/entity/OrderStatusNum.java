package com.yjm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusNum {
    private int waitPay;
    private int waitDeliver;
    private int waitTake;
    private long waitComment;
}
