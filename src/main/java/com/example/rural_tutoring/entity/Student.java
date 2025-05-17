package com.example.rural_tutoring.entity;


import lombok.Data;

@Data
public class Student {
    private String id; // 学生ID（唯一标识）
    private String name; // 学生姓名
    private String gender; // 性别
    private int age; // 年龄
    private String school; // 学校
    private String grade; // 年级
    private String contact; // 学生联系方式
    private String parentContact; // 家长联系方式
}