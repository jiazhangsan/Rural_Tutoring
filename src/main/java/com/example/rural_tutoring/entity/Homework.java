package com.example.rural_tutoring.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Homework {
    private Integer id;          // 作业ID
    private Integer lessonId;    // 课程ID
    private String title;        // 作业标题
    private String content;      // 作业内容
    private LocalDateTime deadline;  // 截止时间
    private Integer createdBy;   // 创建者ID（教师ID）
    private LocalDateTime createdAt; // 创建时间
    private HomeworkStatus status;   // 作业状态
    
    /**
     * 作业附件列表
     * 使用transient注解标记该字段不参与MyBatis的映射
     */
    private transient List<HomeworkAttachment> attachments;
    
    public enum HomeworkStatus {
        DRAFT("草稿"),
        PUBLISHED("已发布"),
        CLOSED("已截止");
        
        private final String description;
        
        HomeworkStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}
