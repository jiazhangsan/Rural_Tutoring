package com.example.rural_tutoring.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HomeworkAttachment {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 作业ID
     */
    private Integer homeworkId;

    /**
     * 文件原始名称
     */
    private String originalFilename;

    /**
     * 文件存储路径
     */
    private String fileUrl;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;

    /**
     * 上传者ID
     */
    private Integer uploadedBy;
} 