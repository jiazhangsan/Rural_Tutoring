package com.example.rural_tutoring.entity;

import lombok.Data;

@Data
public class TeacherStudent {
    private Integer id;         // 主键ID
    private Integer teacherId;  // 关联教师ID（Teachers表的id）
    private String studentId;   // 关联学生ID（Student表的id，类型为String）
    // 可根据需要添加更多字段，如分组、备注等
}