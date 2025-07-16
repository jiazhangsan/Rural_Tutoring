package com.example.rural_tutoring.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LessonImage {
    private Integer id;
    private Integer lessonId;
    private String imageUrl;
    private String originalFilename;
    private LocalDateTime uploadTime;
    // getter/setter
}