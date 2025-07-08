package com.example.rural_tutoring.service.impl;

import com.example.rural_tutoring.entity.Homework;
import com.example.rural_tutoring.entity.HomeworkAttachment;
import com.example.rural_tutoring.entity.Lesson;
import com.example.rural_tutoring.mapper.HomeworkAttachmentMapper;
import com.example.rural_tutoring.mapper.HomeworkMapper;
import com.example.rural_tutoring.mapper.LessonMapper;
import com.example.rural_tutoring.service.HomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HomeworkServiceImpl implements HomeworkService {

    @Autowired
    private HomeworkMapper homeworkMapper;
    
    @Autowired
    private LessonMapper lessonMapper;

    @Autowired
    private HomeworkAttachmentMapper homeworkAttachmentMapper;

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
} 