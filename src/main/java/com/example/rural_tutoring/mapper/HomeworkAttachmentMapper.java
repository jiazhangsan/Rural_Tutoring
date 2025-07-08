package com.example.rural_tutoring.mapper;

import com.example.rural_tutoring.entity.HomeworkAttachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HomeworkAttachmentMapper {
    /**
     * 批量插入作业附件
     */
    int batchInsert(@Param("attachments") List<HomeworkAttachment> attachments);

    /**
     * 根据作业ID查询附件列表
     */
    List<HomeworkAttachment> findByHomeworkId(@Param("homeworkId") Integer homeworkId);

    /**
     * 根据作业ID删除附件
     */
    int deleteByHomeworkId(@Param("homeworkId") Integer homeworkId);

    /**
     * 根据ID删除附件
     */
    int deleteById(@Param("id") Integer id);

    /**
     * 根据ID查询附件
     */
    HomeworkAttachment findById(@Param("id") Integer id);
} 