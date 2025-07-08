package com.example.rural_tutoring.controller;

import com.example.rural_tutoring.config.Result;
import com.example.rural_tutoring.config.UserContextInterceptor;
import com.example.rural_tutoring.entity.Lesson;
import com.example.rural_tutoring.service.LessonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "课程管理", description = "课程相关的CRUD操作")
@RestController
@RequestMapping("/api/lesson")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Operation(summary = "新增课程", description = "创建一个老师的新的课程")
    @PostMapping
    public Result<Lesson> addLesson(@RequestBody Lesson lesson) {

        int i = lessonService.addLesson(lesson);
        if (i > 0) {
            return Result.success(lesson);
        } else {
            return Result.error("添加失败");
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

        try {
            lessonService.joinLesson(lessonId, currentUserId);
            return Result.success(null);
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }




}
