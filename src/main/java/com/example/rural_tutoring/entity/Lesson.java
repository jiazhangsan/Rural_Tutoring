package com.example.rural_tutoring.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class Lesson {
    private Integer id;           // 课程唯一ID
    
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private LocalTime endTime;
    
    private BigDecimal fee;       // 课时费
    private LessonStatus status;  // 课程状态
    private String notes;         // 课程描述
    private String location;      // 上课地点
    private Integer maxStudents;  // 最大容纳学生数
    private Integer actualStudents; // 实际参与学生数

    /**
     * 课程名称：清晰、具体，能够准确反映课程内容。
     * 课程类型：如一对一辅导、小班教学、大班授课等。
     * 课程时长：每次课程的时长，以及课程总时长。
     * 课程大纲：详细的课程内容安排，包括每次课程的主题和教学重点。
     * 教材使用：指定使用的教材、参考书目或电子资料。
     */
    

    // 课程状态枚举
    public enum LessonStatus {
        ACTIVE("进行中"),
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
