package com.example.rural_tutoring.service;

import com.example.rural_tutoring.entity.Homework;
import com.example.rural_tutoring.entity.HomeworkAttachment;
import com.example.rural_tutoring.entity.HomeworkImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HomeworkService {
    /**
     * 添加作业
     * @param homework 作业信息
     * @return 操作结果
     */
    int addHomework(Homework homework);

    /**
     * 添加作业（包含附件）
     * @param homework 作业信息（包含附件列表）
     * @return 是否添加成功
     */
    boolean addHomeworkWithAttachments(Homework homework);

    /**
     * 获取作业信息
     * @param id 作业ID
     * @return 作业信息
     */
    Homework getHomework(Integer id);

    /**
     * 获取作业附件信息
     * @param attachmentId 附件ID
     * @return 附件信息
     */
    HomeworkAttachment getHomeworkAttachment(Integer attachmentId);


    /**
     * 学生提交作业图片
     * @param homeworkId 作业ID
     * @param images 作业完成图片
     * @return 作业附件信息列表
     */
    List<HomeworkImage> submitHomeworkImages(Integer homeworkId, MultipartFile[] images);

    /**
     * 根据条件查询作业
     * @param lessonId 课程ID（可选）
     * @param status 作业状态（可选）
     * @return 作业列表
     */
    List<Homework> getHomeworksByCondition(Integer lessonId, Homework.HomeworkStatus status);

    /**
     * 查询学生加入课程的所有作业
     * @param studentId 学生ID
     * @param lessonId 课程ID（可选）
     * @param status 作业状态（可选）
     * @return 作业列表
     */
    List<Homework> getStudentHomeworks(Integer studentId, Integer lessonId, Homework.HomeworkStatus status);
} 