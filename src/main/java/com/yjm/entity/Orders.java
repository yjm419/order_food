package com.yjm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {
    private int id;
    private  int uId;
    private int addrId;
    @JsonFormat(pattern = "yyyy-MM-dd:HH:mm:ss",timezone = "GMT+8")
    private Date time;
    private BigDecimal tMoney;
    private String number;
    private int isPay;
    private int status;
}
