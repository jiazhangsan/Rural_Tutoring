package com.example.rural_tutoring.service.impl;

import com.example.rural_tutoring.entity.Homework;
import com.example.rural_tutoring.entity.HomeworkAttachment;
import com.example.rural_tutoring.entity.HomeworkImage;
import com.example.rural_tutoring.entity.Lesson;
import com.example.rural_tutoring.mapper.HomeworkAttachmentMapper;
import com.example.rural_tutoring.mapper.HomeworkMapper;
import com.example.rural_tutoring.mapper.LessonMapper;
import com.example.rural_tutoring.service.HomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.example.rural_tutoring.config.UserContextInterceptor;
import com.example.rural_tutoring.mapper.StudentMapper;
import com.example.rural_tutoring.service.FileService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import com.example.rural_tutoring.mapper.HomeworkImageMapper;

@Service
public class HomeworkServiceImpl implements HomeworkService {

    @Autowired
    private HomeworkMapper homeworkMapper;
    
    @Autowired
    private LessonMapper lessonMapper;

    @Autowired
    private HomeworkAttachmentMapper homeworkAttachmentMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private HomeworkImageMapper homeworkImageMapper;


    @Override
    @Transactional
    public int addHomework(Homework homework) {
        // 1. 检查课程是否存在
        Lesson lesson = lessonMapper.selectLessonById(homework.getLessonId());
        if (lesson == null) {
            throw new RuntimeException("课程不存在");
        }

        // 3. 添加作业
        return homeworkMapper.insertHomework(homework);
    }

    @Override
    @Transactional
    public boolean addHomeworkWithAttachments(Homework homework) {
        // 1. 保存作业基本信息
        int result = homeworkMapper.insert(homework);
        if (result <= 0) {
            return false;
        }

        // 2. 如果有附件，保存附件信息
        List<HomeworkAttachment> attachments = homework.getAttachments();
        if (attachments != null && !attachments.isEmpty()) {
            // 设置作业ID
            for (HomeworkAttachment attachment : attachments) {
                attachment.setHomeworkId(homework.getId());
            }
            // 批量插入附件
            result = homeworkAttachmentMapper.batchInsert(attachments);
            if (result <= 0) {
                throw new RuntimeException("保存附件信息失败");
            }
        }

        return true;
    }

    @Override
    public Homework getHomework(Integer id) {
        // 1. 获取作业基本信息
        Homework homework = homeworkMapper.selectById(id);
        if (homework != null) {
            // 2. 获取作业的附件列表
            List<HomeworkAttachment> attachments = homeworkAttachmentMapper.findByHomeworkId(id);
            homework.setAttachments(attachments);
        }
        return homework;
    }

    @Override
    public HomeworkAttachment getHomeworkAttachment(Integer attachmentId) {
        // 这里需要添加一个新的mapper方法
        return homeworkAttachmentMapper.findById(attachmentId);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HomeworkImage> submitHomeworkImages(Integer homeworkId, MultipartFile[] images) {
        // 获取当前登录的学生ID
        Integer currentUserId = UserContextInterceptor.getCurrentUserId();
        if (currentUserId == null) {
            throw new IllegalStateException("未获取到当前用户信息");
        }

        // 验证学生身份
        Integer studentId = studentMapper.getStudentIdByUserId(currentUserId);
        if (studentId == null) {
            throw new IllegalStateException("当前用户不是学生");
        }

        List<HomeworkImage> homeworkImages = new ArrayList<>();
        try {
            for (MultipartFile image : images) {
                // 上传图片，获取相对路径
                String objectName = fileService.uploadFile(image, "homework");

                // 创建作业图片记录
                HomeworkImage homeworkImage = new HomeworkImage();
                homeworkImage.setHomeworkId(homeworkId);
                homeworkImage.setStudentId(studentId);
                homeworkImage.setImageUrl(objectName);  // 存储相对路径
                homeworkImage.setUploadTime(LocalDateTime.now());
                homeworkImage.setStatus("SUBMITTED");

                // 保存图片记录
                homeworkImageMapper.insertHomeworkImage(homeworkImage);
                homeworkImages.add(homeworkImage);
            }

            return homeworkImages;
        } catch (RuntimeException e) {
            throw new RuntimeException("图片上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Homework> getHomeworksByCondition(Integer lessonId, Homework.HomeworkStatus status) {
        String statusStr = status != null ? status.name() : null;
        return homeworkMapper.getHomeworksByCondition(lessonId, statusStr);
    }

    @Override
    public List<Homework> getStudentHomeworks(Integer studentId, Integer lessonId, Homework.HomeworkStatus status) {
        String statusStr = status != null ? status.name() : null;
        return homeworkMapper.getStudentHomeworks(studentId, lessonId, statusStr);
    }
} 