package com.yjm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Store {
    private int id;
    private String name;
    private String password;
    private String phone;
    private String account;
    private String image;
}
