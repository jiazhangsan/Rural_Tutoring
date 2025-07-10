package com.example.rural_tutoring.mapper;

import com.example.rural_tutoring.entity.Subject;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SubjectMapper {
    int insertSubject(Subject subject);
} 