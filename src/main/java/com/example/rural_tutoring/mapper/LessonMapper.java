package com.example.rural_tutoring.mapper;

import com.example.rural_tutoring.entity.Lesson;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LessonMapper {
    int insertLesson(Lesson lesson);

    int insertTeacherLesson(@Param("lessonId") Integer lessonId, @Param("teacherId") Integer teacherId);

    /**
     * 根据ID查询课程
     */
    Lesson selectLessonById(@Param("id") Integer id);

    /**
     * 检查学生是否已经加入课程
     */
    int checkStudentJoined(@Param("lessonId") Integer lessonId, @Param("studentId") Integer studentId);

    /**
     * 创建学生-课程关联
     */
    int insertLessonStudent(@Param("lessonId") Integer lessonId, @Param("studentId") Integer studentId);

    /**
     * 更新课程实际学生数
     */
    int updateActualStudents(@Param("id") Integer id, @Param("actualStudents") Integer actualStudents);
}