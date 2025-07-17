package com.example.rural_tutoring.service.impl;

import com.example.rural_tutoring.entity.Teacher;
import com.example.rural_tutoring.entity.Lesson;
import com.example.rural_tutoring.config.Result;
import com.example.rural_tutoring.entity.User;
import com.example.rural_tutoring.mapper.TeacherMapper;
import com.example.rural_tutoring.model.DTO.TeacherDTO;
import com.example.rural_tutoring.model.PageResult;
import com.example.rural_tutoring.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    @Transactional
    public int addTeacher(TeacherDTO teacherDTO) {

        int i = teacherMapper.insertTeacher(teacherDTO);
        int j = teacherMapper.insertTeacherSubject(teacherDTO);

        // 这里如果任意一步抛出异常，整个方法会回滚，两个操作都不会生效
        return i + j; // 或者你自己的返回逻辑
    }

    @Override
    public PageResult<Teacher> findTeacher(Integer page, Integer size, String realName, String subject) {

        int offset = (page - 1) * size;
        List<Teacher> teachers = teacherMapper.findTeacherByCondition(offset, size, realName, subject);
        long total = teacherMapper.countTeacher(realName, subject);
        return new PageResult<>(teachers, total, page, size);
    }

    @Override
    public List<Lesson> findLessonByTeacherId(Long teacherId) {
        return teacherMapper.findLessonByTeacherId(teacherId);
    }
} 