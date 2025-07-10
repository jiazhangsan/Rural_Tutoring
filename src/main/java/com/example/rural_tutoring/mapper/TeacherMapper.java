package com.example.rural_tutoring.mapper;

import com.example.rural_tutoring.entity.Teacher;
import com.example.rural_tutoring.model.DTO.TeacherDTO;
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
}