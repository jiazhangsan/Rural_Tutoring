package com.example.rural_tutoring.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @author jiashuhang
 * @description 学生信息认证
 * @date 2025-07-04 19:14
 **/
@Data
public class Student {
    private Integer id; // 学生ID（唯一标识）
    private String name; // 学生姓名
    private String gender; // 性别
    private int age; // 年龄
    private String school; // 学校
    private String grade; // 年级
    private String parentContact; // 家长联系方式
    private Integer userId; // 用户id
}