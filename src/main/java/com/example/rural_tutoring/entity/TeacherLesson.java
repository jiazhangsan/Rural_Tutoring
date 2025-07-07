package com.example.rural_tutoring.entity;

import lombok.Data;

@Data
public class TeacherLesson {
    private Integer id;         // 主键ID
    private Integer teacherId;  // 关联教师表的id
    private Integer lessonId;   // 关联课程表的id
    // 你可以根据需要添加更多字段，比如授课时间、备注等
}