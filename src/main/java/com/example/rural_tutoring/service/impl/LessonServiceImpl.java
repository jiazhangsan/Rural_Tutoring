package com.example.rural_tutoring.service.impl;

import com.example.rural_tutoring.config.Result;
import com.example.rural_tutoring.config.UserContextInterceptor;
import com.example.rural_tutoring.entity.Lesson;
import com.example.rural_tutoring.mapper.LessonMapper;
import com.example.rural_tutoring.mapper.TeacherMapper;
import com.example.rural_tutoring.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonMapper lessonMapper;
    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    @Transactional  // 添加事务管理
    public int addLesson(Lesson lesson) {
        // 获取当前登录用户ID
        Integer currentUserId = UserContextInterceptor.getCurrentUserId();
        if (currentUserId == null) {
            return 0;
        }
        // 查询教师表中是当前用户id的老师id
        Integer teacherId = teacherMapper.getTeacherIdByUserId(currentUserId);
        // 在课程表中添加课程
        int i = lessonMapper.insertLesson(lesson);

        int j = lessonMapper.insertTeacherLesson(lesson.getId(), teacherId);

        // 在老师课程关联表中添加关联关系
        if (i > 0 && j > 0) {
            return 1;
        } else {
            throw new RuntimeException("插入课程信息失败或插入教师课程关联失败");
        }
    }

    @Override
    @Transactional
    public int joinLesson(Integer lessonId, Integer studentId) {
        // 1. 检查课程是否存在
        Lesson lesson = lessonMapper.selectLessonById(lessonId);
        if (lesson == null) {
            throw new RuntimeException("课程不存在");
        }

        // 2. 检查课程是否还有空位
        if (lesson.getActualStudents() >= lesson.getMaxStudents()) {
            throw new RuntimeException("课程已满");
        }

        // 3. 检查学生是否已经加入过这个课程
        int joined = lessonMapper.checkStudentJoined(lessonId, studentId);
        if (joined > 0) {
            throw new RuntimeException("已经加入过这个课程");
        }

        // 4. 创建学生-课程关联
        int i = lessonMapper.insertLessonStudent(lessonId, studentId);
        if (i <= 0) {
            throw new RuntimeException("加入课程失败");
        }

        // 5. 更新课程实际学生数
        lesson.setActualStudents(lesson.getActualStudents() + 1);
        int j = lessonMapper.updateActualStudents(lessonId, lesson.getActualStudents());
        if (j <= 0) {
            throw new RuntimeException("更新课程学生数失败");
        }

        return 1;
    }
} 