package com.example.rural_tutoring.service.impl;

import com.example.rural_tutoring.entity.Teacher;
import com.example.rural_tutoring.mapper.TeacherMapper;
import com.example.rural_tutoring.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public int addTeacher(Teacher teacher) {
        return teacherMapper.insertTeacher(teacher);
    }
} 