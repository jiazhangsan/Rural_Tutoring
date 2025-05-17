package com.example.rural_tutoring.mapper;

import com.example.rural_tutoring.entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {

    int insert(Student student);

    @Delete("DELETE FROM student WHERE id = #{id}")
    int deleteById(String id);


    int update(Student student);

    @Select("SELECT * FROM student WHERE id = #{id}")
    Student selectById(String id);

    @Select("SELECT * FROM student")
    List<Student> selectAll();
}