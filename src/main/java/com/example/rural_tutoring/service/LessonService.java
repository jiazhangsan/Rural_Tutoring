package com.example.rural_tutoring.service;

import com.example.rural_tutoring.entity.Lesson;

public interface LessonService {
    int addLesson(Lesson lesson);
    
    /**
     * 学生加入课程
     * @param lessonId 课程ID
     * @param studentId 学生ID
     * @return 操作结果
     */
    int joinLesson(Integer lessonId, Integer studentId);
} 