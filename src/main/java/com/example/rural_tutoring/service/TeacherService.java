package com.example.rural_tutoring.service;

import com.example.rural_tutoring.entity.Teacher;
import com.example.rural_tutoring.model.DTO.TeacherDTO;
import com.example.rural_tutoring.model.PageResult;




public interface TeacherService {
    int addTeacher(TeacherDTO teacherDTO);
    PageResult<Teacher> findTeacher(Integer page, Integer size, String realName, String subject);
} 