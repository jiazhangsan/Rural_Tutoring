package com.example.rural_tutoring.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

@Data
public class Lessons {
    private Integer id;           // 课程唯一ID
    private Date date;            // 上课日期
    private Time startTime;       // 开始时间
    private Time endTime;         // 结束时间
    private BigDecimal fee;       // 课时费
    private String status;        // 状态（如"已完成"或"已取消"）
    private String notes;         // 课堂笔记（记录学生问题等）
}
