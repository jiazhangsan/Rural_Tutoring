package com.example.rural_tutoring.mapper;

import com.example.rural_tutoring.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeacherMapper {
    int insertTeacher(Teacher teacher);
} 