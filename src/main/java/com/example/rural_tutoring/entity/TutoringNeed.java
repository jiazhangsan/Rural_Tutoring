package com.example.rural_tutoring.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TutoringNeed {
    private Integer id;
    private Integer studentId;      // 发布需求的学生ID
    private String subject;         // 需要辅导的科目
    private String grade;           // 学生年级
    private String description;     // 具体需求描述
    private String location;        // 上课地点
    private Integer expectedPrice;  // 期望价格（每小时）
    private String timeRequirement; // 时间要求
    private String contactPhone;    // 联系电话
    private Integer status;         // 状态：0-待匹配 1-已匹配 2-已完成 3-已取消
    private LocalDateTime createTime; // 发布时间
    private LocalDateTime updateTime; // 更新时间
} 