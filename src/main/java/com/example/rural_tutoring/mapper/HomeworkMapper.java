package com.example.rural_tutoring.mapper;

import com.example.rural_tutoring.entity.Homework;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HomeworkMapper {
    /**
     * 插入作业
     */
    int insert(Homework homework);

    /**
     * 根据ID查询作业
     */
    Homework selectById(@Param("id") Integer id);

    /**
     * 插入作业（旧方法，保持兼容）
     */
    int insertHomework(Homework homework);

    /**
     * 根据条件查询作业
     * @param lessonId 课程ID（可选）
     * @param status 作业状态（可选）
     * @return 作业列表
     */
    List<Homework> getHomeworksByCondition(@Param("lessonId") Integer lessonId, 
                                          @Param("status") String status);

    /**
     * 查询学生加入课程的所有作业
     * @param studentId 学生ID
     * @param lessonId 课程ID（可选）
     * @param status 作业状态（可选）
     * @return 作业列表
     */
    List<Homework> getStudentHomeworks(@Param("studentId") Integer studentId,
                                      @Param("lessonId") Integer lessonId,
                                      @Param("status") String status);
} 