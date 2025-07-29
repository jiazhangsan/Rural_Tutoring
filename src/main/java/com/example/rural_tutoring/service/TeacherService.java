package com.example.rural_tutoring.service;

import com.example.rural_tutoring.entity.Teacher;
import com.example.rural_tutoring.entity.Lesson;
import com.example.rural_tutoring.model.DTO.TeacherDTO;
import com.example.rural_tutoring.model.DTO.TeacherSearchDTO;
import com.example.rural_tutoring.model.PageResult;
import com.example.rural_tutoring.model.VO.TeacherVO;

import java.util.List;

public interface TeacherService {
    int addTeacher(TeacherDTO teacherDTO);
    PageResult<Teacher> findTeacher(Integer page, Integer size, String realName, String subject);
    List<Lesson> findLessonByTeacherId(Long teacherId);
    
    /**
     * 基于地理位置和条件搜索教师
     * @param searchDTO 搜索条件
     * @return 分页结果
     */
    PageResult<TeacherVO> findTeacherByLocationAndCondition(TeacherSearchDTO searchDTO);
    
    /**
     * 更新教师地理位置信息
     * @param teacher 教师信息
     * @return 更新结果
     */
    int updateTeacherLocation(Teacher teacher);
} 