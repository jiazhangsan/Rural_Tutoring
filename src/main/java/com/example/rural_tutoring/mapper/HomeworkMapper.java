package com.example.rural_tutoring.mapper;

import com.example.rural_tutoring.entity.Homework;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HomeworkMapper {
    /**
     * 插入作业
     */
    int insert(Homework homework);

    /**
     * 根据ID查询作业
     */
    Homework selectById(@Param("id") Integer id);

    /**
     * 插入作业（旧方法，保持兼容）
     */
    int insertHomework(Homework homework);
} 