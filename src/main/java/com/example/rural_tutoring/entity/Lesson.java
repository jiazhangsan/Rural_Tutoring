package com.example.rural_tutoring.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class Lesson {
    private Integer id;           // 课程唯一ID
    private LocalDate date;       // 上课日期
    private LocalTime startTime;  // 开始时间
    private LocalTime endTime;    // 结束时间
    private BigDecimal fee;       // 课时费
    private LessonStatus status;  // 课程状态
    private String notes;         // 课程描述
    private String location;      // 上课地点
    private Integer maxStudents;  // 最大容纳学生数
    private Integer actualStudents; // 实际参与学生数
    

    // 课程状态枚举
    public enum LessonStatus {
        SCHEDULED("已排课"),
        IN_PROGRESS("进行中"),
        COMPLETED("已结课"),
        CANCELLED("已取消"),
        POSTPONED("已延期");

        private final String description;

        LessonStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
