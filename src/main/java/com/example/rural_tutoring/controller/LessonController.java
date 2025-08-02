package com.example.rural_tutoring.controller;

import com.example.rural_tutoring.config.Result;
import com.example.rural_tutoring.config.UserContextInterceptor;
import com.example.rural_tutoring.entity.Lesson;
import com.example.rural_tutoring.entity.LessonImage;
import com.example.rural_tutoring.entity.Student;
import com.example.rural_tutoring.model.VO.LessonVO;
import com.example.rural_tutoring.service.FileService;
import com.example.rural_tutoring.service.LessonService;
import com.example.rural_tutoring.service.StudentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.BeanUtils;

@Tag(name = "课程管理", description = "课程相关的CRUD操作")
@RestController
@RequestMapping("/api/lesson")
public class LessonController {

    @Autowired
    private LessonService lessonService;
    @Autowired
    private FileService fileService;
    @Autowired
    private ObjectMapper objectMapper;  // 注入配置好的 ObjectMapper
    @Autowired
    private StudentService studentService;

    @Operation(summary = "新增课程", description = "创建一个老师的新的课程，支持上传多张图片")
    @PostMapping("/addLesson")
    public Result<LessonVO> addLesson(
            @RequestPart("lesson") String lessonJson,
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) {
        try {
            // 使用注入的 ObjectMapper
            Lesson lesson = objectMapper.readValue(lessonJson, Lesson.class);
            
            // 创建并复制属性到 LessonVO
            LessonVO lessonVO = new LessonVO();
            BeanUtils.copyProperties(lesson, lessonVO);
            
            // 处理图片上传
            List<LessonImage> lessonImages = new ArrayList<>();
            List<String> imageUrls = new ArrayList<>();

            if (images != null && images.length > 0) {
                for (MultipartFile image : images) {
                    if (!isValidImageType(image.getOriginalFilename())) {
                        return Result.error("不支持的图片格式：" + image.getOriginalFilename());
                    }

                    // 上传图片
                    String imageUrl = fileService.uploadFile(image, "lesson");
                    imageUrls.add(imageUrl);

                    // 创建图片记录
                    LessonImage lessonImage = new LessonImage();
                    lessonImage.setOriginalFilename(image.getOriginalFilename());
                    lessonImage.setImageUrl(imageUrl);
                    lessonImage.setUploadTime(LocalDateTime.now());
                    lessonImages.add(lessonImage);
                }
            }

            // 设置图片URL列表
            lessonVO.setImagesUrl(imageUrls);

            // 保存课程和图片信息
            int result = lessonService.addLessonWithImages(lesson, lessonImages);
            if (result > 0) {
                return Result.success(lessonVO);
            } else {
                return Result.error("添加失败");
            }
        } catch (IOException e) {
            return Result.error("数据处理失败：" + e.getMessage());
        }
    }


    @Operation(summary = "加入课程", description = "学生加入课程")
    @PostMapping("/join/{lessonId}")
    public Result<Void> joinLesson(@PathVariable Integer lessonId) {
        // 获取当前登录用户ID（学生ID）
        Integer currentUserId = UserContextInterceptor.getCurrentUserId();
        System.out.println("当前的登录用户id："+currentUserId);
        if (currentUserId == null) {
            return Result.error("用户ID不能为空");
        }
        // 根据用户id查询学生表中的学生id
        Student student = studentService.getStudentByUserId(currentUserId);
        Integer studentId = student.getId();
        if (student == null) {
            return Result.error("学生不存在");
        }

        try {
            lessonService.joinLesson(lessonId, studentId);
            return Result.success(null);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "编辑课程信息", description = "编辑课程信息，支持更新课程信息和图片")
    @PutMapping("/updateLesson")
    public Result<LessonVO> updateLesson(
            @RequestPart("lesson") String lessonJson,
            @RequestPart(value = "images", required = false) MultipartFile[] images
    ) {
        try {
            // 使用注入的 ObjectMapper 解析课程信息
            Lesson lesson = objectMapper.readValue(lessonJson, Lesson.class);
            
            // 验证课程是否存在
            if (lesson.getId() == null) {
                return Result.error("课程ID不能为空");
            }
            
            // 创建并复制属性到 LessonVO
            LessonVO lessonVO = new LessonVO();
            BeanUtils.copyProperties(lesson, lessonVO);
            
            // 处理图片上传
            List<LessonImage> lessonImages = new ArrayList<>();
            List<String> imageUrls = new ArrayList<>();

            if (images != null) {
                for (MultipartFile image : images) {
                    if (!isValidImageType(image.getOriginalFilename())) {
                        return Result.error("不支持的图片格式：" + image.getOriginalFilename());
                    }

                    // 上传图片
                    String imageUrl = fileService.uploadFile(image, "lesson");
                    imageUrls.add(imageUrl);

                    // 创建图片记录
                    LessonImage lessonImage = new LessonImage();
                    lessonImage.setOriginalFilename(image.getOriginalFilename());
                    lessonImage.setImageUrl(imageUrl);
                    lessonImage.setUploadTime(LocalDateTime.now());
                    lessonImage.setLessonId(lesson.getId());
                    lessonImages.add(lessonImage);
                }
            }

            // 设置图片URL列表
            lessonVO.setImagesUrl(imageUrls);

            // 更新课程和图片信息
            int result = lessonService.updateLessonWithImages(lesson, lessonImages);
            if (result > 0) {
                return Result.success(lessonVO);
            } else {
                return Result.error("更新失败");
            }
        } catch (IOException e) {
            return Result.error("数据处理失败：" + e.getMessage());
        }
    }



    @Operation(summary = "查询指定课程下的学生", description = "查询指定课程下的学生")
    @GetMapping("/getStudents/{lessonId}")
    public Result<List<Student>> getStudents(@PathVariable Integer lessonId) {
        List<Student> students = lessonService.getStudents(lessonId);
        return Result.success(students);
    }



    private boolean isValidImageType(String filename) {
        if (filename == null) return false;
        filename = filename.toLowerCase();
        return filename.endsWith(".jpg") ||
                filename.endsWith(".jpeg") ||
                filename.endsWith(".png") ||
                filename.endsWith(".gif");
    }
    @Operation(summary = "上传视频", description = "上传视频到课程")
    @PostMapping("/uploadVideo")
    public Result<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            // 验证文件类型
            if (!isValidVideoType(file.getOriginalFilename())) {
                return Result.error("不支持的文件类型，仅支持视频格式（mp4, avi, mov, wmv, flv, webm, mkv）");
            }

            // 验证文件大小（限制为500MB）
            if (file.getSize() > 500 * 1024 * 1024) {
                return Result.error("文件大小不能超过500MB");
            }

            // 验证文件是否为空
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }

            String videoUrl = fileService.uploadFile(file, "video");
            return Result.success(videoUrl);
        } catch (Exception e) {
            return Result.error("上传失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据条件筛选所有课程", description = "根据条件筛选所有课程")
    @GetMapping("/getAllLessons")
    public Result<List<Lesson>> getAllLessons(@RequestParam(required = false) Integer subjectId,
                                              @RequestParam(required = false) String lessonName,
                                              @RequestParam(required = false) Integer gradeId) {
        List<Lesson> lessons = lessonService.getAllLessons(subjectId, lessonName, gradeId);
        return Result.success(lessons);
    }

    @Operation(summary = "查询当前学生加入的所有课程", description = "查询当前登录学生加入的所有课程")
    @GetMapping("/getMyJoinedLessons")
    public Result<List<Lesson>> getMyJoinedLessons() {
        // 获取当前登录用户ID
        Integer currentUserId = UserContextInterceptor.getCurrentUserId();
        if (currentUserId == null) {
            return Result.error("未获取到用户信息，请重新登录");
        }

        // 根据用户ID获取学生信息
        Student student = studentService.getStudentByUserId(currentUserId);
        if (student == null) {
            return Result.error("未找到对应的学生信息");
        }

        // 查询学生加入的所有课程
        List<Lesson> lessons = lessonService.getStudentJoinedLessons(student.getId());
        return Result.success(lessons);
    }


    /**
     * 验证视频文件类型
     */
    private boolean isValidVideoType(String filename) {
        if (filename == null) {
            return false;
        }
        String extension = filename.toLowerCase();
        return extension.endsWith(".mp4") || 
               extension.endsWith(".avi") || 
               extension.endsWith(".mov") || 
               extension.endsWith(".wmv") || 
               extension.endsWith(".flv") || 
               extension.endsWith(".webm") || 
               extension.endsWith(".mkv");
    }




}
