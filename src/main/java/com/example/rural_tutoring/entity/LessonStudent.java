package com.example.rural_tutoring.entity;

import lombok.Data;

@Data
public class LessonStudent {
    private Integer id;         // 主键ID
    private Integer lessonId;   // 关联课程ID
    private String studentId;   // 关联学生ID（与Student类的id类型保持一致）
    // 可根据需要添加更多字段，如选课时间、备注等
}