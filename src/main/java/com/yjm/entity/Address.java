package com.yjm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private int id;
    private int uId;
    private String name;
    private String phone;
    private String addr;
    private String addDetails;
    private int isDefault;
}
