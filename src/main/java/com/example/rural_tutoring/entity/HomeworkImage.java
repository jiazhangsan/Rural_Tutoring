package com.example.rural_tutoring.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HomeworkImage {
    private Integer id;              // 主键ID
    private Integer homeworkId;      // 作业ID
    private Integer studentId;       // 学生ID
    private String imageUrl;         // 图片URL
    private LocalDateTime uploadTime;// 上传时间
    private String status;          // 状态（例如：已提交、已批改等）
    private String comment;         // 教师评语
}
