package com.example.rural_tutoring.entity;

import lombok.Data;
import java.util.Date;

@Data
public class User {
    private Integer id;           // 用户唯一ID
    private String username;      // 昵称
    private String account;      // 账号
    private String passwordHash;  // 加密后的密码
    private Date createdAt;       // 注册时间
    private String openid;
}
