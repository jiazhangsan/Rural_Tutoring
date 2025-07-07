package com.example.rural_tutoring.entity;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;
import java.util.Date;

@Data
public class Homeworks {
    private Integer id;              // 作业唯一ID
    private Integer studentId;       // 关联学生ID
    private Integer lessonId;        // 关联课程ID（可选）
    private String title;            // 作业标题
    private String description;      // 作业描述
    private String attachmentUrl;    // 作业附件链接（如PDF）
    private Date deadline;           // 截止时间
    private String status;           // 状态（如"未提交"或"已批改"）
    private Integer score;           // 评分（百分制）
    private String feedback;         // 教师评语
    /**
     * 教师的id
     */
    private Integer teacher_id;
}
