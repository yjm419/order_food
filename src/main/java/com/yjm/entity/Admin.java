package com.yjm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    private int id;
    private String name;
    private String password;
    private String phone;
    private String account;
    private int limits;
}
