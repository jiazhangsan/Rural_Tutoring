package com.example.rural_tutoring.service.impl;

import com.example.rural_tutoring.config.UserContextInterceptor;
import com.example.rural_tutoring.entity.Lesson;
import com.example.rural_tutoring.entity.LessonImage;
import com.example.rural_tutoring.mapper.LessonImageMapper;
import com.example.rural_tutoring.mapper.LessonMapper;
import com.example.rural_tutoring.mapper.TeacherMapper;
import com.example.rural_tutoring.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.example.rural_tutoring.entity.Student;

@Service
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonMapper lessonMapper;
    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private LessonImageMapper lessonImageMapper;

    /**
     * 添加课程及其相关图片
     * @param lesson 课程信息
     * @param lessonImages 课程图片列表
     * @return 操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)  // 确保所有异常都会触发回滚
    public int addLessonWithImages(Lesson lesson, List<LessonImage> lessonImages) {
        // 获取当前登录用户ID
        Integer currentUserId = UserContextInterceptor.getCurrentUserId();
        if (currentUserId == null) {
            throw new IllegalStateException("未获取到当前用户信息");
        }

        // 查询教师表中是当前用户id的老师id
        Integer teacherId = teacherMapper.getTeacherIdByUserId(currentUserId);
        if (teacherId == null) {
            throw new IllegalStateException("当前用户不是教师");
        }


        // 保存课程
        int lessonResult = lessonMapper.insertLesson(lesson);
        if (lessonResult <= 0) {
            throw new RuntimeException("保存课程信息失败");
        }

        // 在老师课程关联表中添加数据
        int teacherLessonResult = lessonMapper.insertTeacherLesson(lesson.getId(), teacherId);
        if (teacherLessonResult <= 0) {
            throw new RuntimeException("保存教师课程关联信息失败");
        }

        // 保存图片记录
        if (lessonImages != null && !lessonImages.isEmpty()) {
            for (LessonImage image : lessonImages) {
                image.setLessonId(lesson.getId());
                int imageResult = lessonImageMapper.insertLessonImage(image);
                if (imageResult <= 0) {
                    throw new RuntimeException("保存课程图片信息失败");
                }
            }
        }

        return lessonResult;
    }



    @Override
    @Transactional
    public int joinLesson(Integer lessonId, Integer studentId) {
        // 1. 检查课程是否存在
        Lesson lesson = lessonMapper.selectLessonById(lessonId);
        if (lesson == null) {
            throw new RuntimeException("课程不存在");
        }

        // 2. 检查课程是否还有空位
        if (lesson.getActualStudents() >= lesson.getMaxStudents()) {
            throw new RuntimeException("课程已满");
        }

        // 3. 检查学生是否已经加入过这个课程
        int joined = lessonMapper.checkStudentJoined(lessonId, studentId);
        if (joined > 0) {
            throw new RuntimeException("已经加入过这个课程");
        }

        // 4. 创建学生-课程关联
        int i = lessonMapper.insertLessonStudent(lessonId, studentId);
        if (i <= 0) {
            throw new RuntimeException("加入课程失败");
        }

        // 5. 更新课程实际学生数
        lesson.setActualStudents(lesson.getActualStudents() + 1);
        int j = lessonMapper.updateActualStudents(lessonId, lesson.getActualStudents());
        if (j <= 0) {
            throw new RuntimeException("更新课程学生数失败");
        }

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateLessonWithImages(Lesson lesson, List<LessonImage> lessonImages) {
        // 验证课程是否存在
        Lesson existingLesson = lessonMapper.selectLessonById(lesson.getId());
        if (existingLesson == null) {
            throw new RuntimeException("课程不存在");
        }

        // 验证当前用户是否有权限修改该课程
        Integer currentUserId = UserContextInterceptor.getCurrentUserId();
        if (currentUserId == null) {
            throw new IllegalStateException("未获取到当前用户信息");
        }
        
        Integer teacherId = teacherMapper.getTeacherIdByUserId(currentUserId);
        if (teacherId == null) {
            throw new IllegalStateException("当前用户不是教师");
        }
        
        // 验证是否是课程的创建者
        boolean isTeacherOfLesson = lessonMapper.isTeacherOfLesson(lesson.getId(), teacherId);
        if (!isTeacherOfLesson) {
            throw new IllegalStateException("没有权限修改该课程");
        }

        // 更新课程基本信息
        int result = lessonMapper.updateLesson(lesson);
        if (result <= 0) {
            throw new RuntimeException("更新课程信息失败");
        }

        // 处理新上传的图片
        if (lessonImages != null && !lessonImages.isEmpty()) {
            for (LessonImage image : lessonImages) {
                int imageResult = lessonImageMapper.insertLessonImage(image);
                if (imageResult <= 0) {
                    throw new RuntimeException("保存课程图片信息失败");
                }
            }
        }

        return result;
    }

    @Override
    public List<Student> getStudents(Integer lessonId) {
        // 验证课程是否存在
        Lesson lesson = lessonMapper.selectLessonById(lessonId);
        if (lesson == null) {
            throw new RuntimeException("课程不存在");
        }
        
        // 获取当前用户ID
        Integer currentUserId = UserContextInterceptor.getCurrentUserId();
        if (currentUserId == null) {
            throw new IllegalStateException("未获取到当前用户信息");
        }
        
        // 获取教师ID
        Integer teacherId = teacherMapper.getTeacherIdByUserId(currentUserId);
        
        // 如果是教师，验证是否是该课程的教师
        if (teacherId != null) {
            boolean isTeacherOfLesson = lessonMapper.isTeacherOfLesson(lessonId, teacherId);
            if (!isTeacherOfLesson) {
                throw new IllegalStateException("没有权限查看该课程的学生信息");
            }
        }
        
        // 查询课程下的学生列表
        return lessonMapper.getStudentsByLessonId(lessonId);
    }

    @Override
    public List<Lesson> getAllLessons(Integer subjectId, String lessonName, Integer gradeId) {
        return lessonMapper.getAllLessons(subjectId, lessonName, gradeId);
    }

    @Override
    public List<Lesson> getStudentJoinedLessons(Integer studentId) {
        return lessonMapper.getStudentJoinedLessons(studentId);
    }
} 