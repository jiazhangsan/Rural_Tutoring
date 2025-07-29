package com.example.rural_tutoring.mapper;

import com.example.rural_tutoring.entity.Teacher;
import com.example.rural_tutoring.entity.Lesson;
import com.example.rural_tutoring.model.DTO.TeacherDTO;
import com.example.rural_tutoring.model.DTO.TeacherSearchDTO;
import com.example.rural_tutoring.model.VO.TeacherVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TeacherMapper {
    int insertTeacher(TeacherDTO teacherDTO);

    /**
     * 根据用户id查询老师id
     * @param currentUserId
     * @return
     */
    Integer getTeacherIdByUserId(Integer currentUserId);

    List<Teacher> findTeacherByCondition(@Param("offset") int offset, @Param("limit") int limit, @Param("realName") String realName, @Param("subject") String subject);

    long countTeacher(String realName, String subject);

    /**
     * 在teacher_subject表中添加数据
     * @param teacherDTO
     * @return
     */
    int insertTeacherSubject(TeacherDTO teacherDTO);

    /**
     * 根据老师ID查询所有课程
     * @param teacherId 老师ID
     * @return 课程列表
     */
    List<Lesson> findLessonByTeacherId(@Param("teacherId") Long teacherId);
    
    /**
     * 基于地理位置和条件搜索教师
     * @param searchDTO 搜索条件
     * @return 教师列表（包含距离信息）
     */
    List<TeacherVO> findTeacherByLocationAndCondition(TeacherSearchDTO searchDTO);
    
    /**
     * 统计符合条件的教师数量
     * @param searchDTO 搜索条件
     * @return 教师数量
     */
    long countTeacherByLocationAndCondition(TeacherSearchDTO searchDTO);
    
    /**
     * 更新教师地理位置信息
     * @param teacher 教师信息
     * @return 更新结果
     */
    int updateTeacherLocation(Teacher teacher);
}