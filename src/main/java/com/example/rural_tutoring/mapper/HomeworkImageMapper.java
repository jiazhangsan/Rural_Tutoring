package com.example.rural_tutoring.mapper;

import com.example.rural_tutoring.entity.HomeworkImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface HomeworkImageMapper {
    /**
     * 插入作业图片记录
     */
    int insertHomeworkImage(HomeworkImage homeworkImage);

    /**
     * 根据作业ID获取所有图片
     */
    List<HomeworkImage> getImagesByHomeworkId(@Param("homeworkId") Integer homeworkId);

    /**
     * 根据作业ID和学生ID获取图片
     */
    List<HomeworkImage> getImagesByHomeworkAndStudent(
            @Param("homeworkId") Integer homeworkId,
            @Param("studentId") Integer studentId
    );

    /**
     * 更新图片状态和评语
     */
    int updateStatusAndComment(
            @Param("id") Integer id,
            @Param("status") String status,
            @Param("comment") String comment
    );
} 