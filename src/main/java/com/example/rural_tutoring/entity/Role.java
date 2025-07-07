package com.example.rural_tutoring.entity;

import lombok.Data;

@Data
public class Role {
    private Integer id;
    private String roleName; // "教师", "学生"
}
