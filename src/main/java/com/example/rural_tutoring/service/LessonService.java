package com.example.rural_tutoring.service;

import com.example.rural_tutoring.entity.Lesson;
import com.example.rural_tutoring.entity.LessonImage;
import com.example.rural_tutoring.entity.Student;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LessonService {


    /**
     * 学生加入课程
     * @param lessonId 课程ID
     * @param studentId 学生ID
     * @return 操作结果
     */
    int joinLesson(Integer lessonId, Integer studentId);

    /**
     * @author jiashuhang
     * @description 添加课程
     * @date 2025-07-16 11:01
     **/
    int addLessonWithImages(Lesson lesson, List<LessonImage> lessonImages);
    
    /**
     * 更新课程信息和相关图片
     * @param lesson 课程信息
     * @param lessonImages 新上传的图片列表
     * @return 更新结果
     */
    int updateLessonWithImages(Lesson lesson, List<LessonImage> lessonImages);

    /**
     * 查询指定课程下的所有学生
     * @param lessonId 课程ID
     * @return 学生列表
     */
    List<Student> getStudents(Integer lessonId);
}