package com.example.rural_tutoring.mapper;

import com.example.rural_tutoring.entity.TutoringNeed;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface TutoringNeedMapper {
    /**
     * 插入新的家教需求
     * @param tutoringNeed 家教需求信息
     * @return 影响的行数
     */
    int insertTutoringNeed(TutoringNeed tutoringNeed);

    /**
     * 更新家教需求状态
     * @param id 需求ID
     * @param status 新状态
     * @return 影响的行数
     */
    int updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    /**
     * 查询学生发布的所有需求
     * @param studentId 学生ID
     * @return 需求列表
     */
    List<TutoringNeed> findByStudentId(@Param("studentId") Integer studentId);

    /**
     * 根据条件查询家教需求
     * @param subject 科目
     * @param status 状态
     * @return 需求列表
     */
    List<TutoringNeed> findByCondition(@Param("subject") String subject, @Param("status") Integer status);
} 