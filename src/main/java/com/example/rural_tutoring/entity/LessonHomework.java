package com.example.rural_tutoring.entity;

import lombok.Data;

@Data
public class LessonHomework {
    private Integer id;         // 主键ID
    private Integer lessonId;   // 关联课程ID（Lessons表的id）
    private Integer homeworkId; // 关联作业ID（Homeworks表的id）
    // 可根据需要添加更多字段，如布置时间、备注等
}