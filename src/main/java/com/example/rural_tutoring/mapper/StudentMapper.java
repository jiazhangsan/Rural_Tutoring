package com.example.rural_tutoring.mapper;

import com.example.rural_tutoring.entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {

    int insert(Student student);

    @Delete("DELETE FROM student WHERE id = #{id}")
    int deleteById(Integer id);


    int update(Student student);

    @Select("SELECT * FROM student WHERE id = #{id}")
    Student selectById(@Param("id") Integer id);

    @Select("SELECT * FROM student")
    List<Student> selectAll();

    /**
     * 根据用户ID获取学生ID
     */
    Integer getStudentIdByUserId(@Param("userId") Integer userId);
}