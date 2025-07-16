package com.example.rural_tutoring.mapper;

import com.example.rural_tutoring.entity.LessonImage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LessonImageMapper {
    int insertLessonImage(LessonImage image);
}