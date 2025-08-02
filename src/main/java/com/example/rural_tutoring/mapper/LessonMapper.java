package com.example.rural_tutoring.mapper;

import com.example.rural_tutoring.entity.Lesson;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import com.example.rural_tutoring.entity.Student;

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

    /**
     * 检查指定教师是否是课程的创建者
     */
    boolean isTeacherOfLesson(@Param("lessonId") Integer lessonId, @Param("teacherId") Integer teacherId);

    /**
     * 更新课程信息
     */
    int updateLesson(Lesson lesson);

    /**
     * 获取课程下的所有学生
     */
    List<Student> getStudentsByLessonId(@Param("lessonId") Integer lessonId);

    /**
     * 根据条件筛选所有课程
     * @param subjectId 科目ID（可选）
     * @param lessonName 课程名称（对应notes字段，可选）
     * @param gradeId 年级ID（可选）
     * @return 课程列表
     */
    List<Lesson> getAllLessons(@Param("subjectId") Integer subjectId, 
                               @Param("lessonName") String lessonName, 
                               @Param("gradeId") Integer gradeId);

    /**
     * 查询学生加入的所有课程
     * @param studentId 学生ID
     * @return 课程列表
     */
    List<Lesson> getStudentJoinedLessons(@Param("studentId") Integer studentId);
}