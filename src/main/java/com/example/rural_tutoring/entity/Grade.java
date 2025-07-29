package com.example.rural_tutoring.entity;

import lombok.Data;
import java.util.Date;

/**
 * @author jiashuhang
 * @description 年级信息
 * @date 2025-01-XX
 **/
@Data
public class Grade {
    private Integer id;           // 年级ID
    private String description;   // 年级描述
    private Integer sortOrder;    // 排序顺序
    private Date createdAt;       // 创建时间
    private Date updatedAt;       // 更新时间
} 