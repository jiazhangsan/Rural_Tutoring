package com.example.rural_tutoring.entity;

import lombok.Data;

@Data
public class UserRole {
    private Integer id;         // 关联ID（主键）
    private Integer userId;    // 关联users.id
    private Integer roleId; // 关联Role表的ID

}
