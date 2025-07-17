package com.example.rural_tutoring.controller;

import com.example.rural_tutoring.config.Result;
import com.example.rural_tutoring.entity.Lesson;
import com.example.rural_tutoring.entity.Teacher;
import com.example.rural_tutoring.entity.User;
import com.example.rural_tutoring.model.DTO.TeacherDTO;
import com.example.rural_tutoring.model.PageResult;
import com.example.rural_tutoring.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "教师管理", description = "教师相关的CRUD操作")
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Operation(summary = "新增老师", description = "创建一个新的老师")
    @PostMapping
    public Result<TeacherDTO> addTeacher(@RequestBody TeacherDTO teacherDTO) {
        int i = teacherService.addTeacher(teacherDTO);


        if (i > 0) {
            return Result.success(teacherDTO);
        } else {
            return Result.error("添加失败");
        }
    }


    @Operation(summary = "查找老师", description = "这是一个分页，老师名，和科目模糊查询")
    @GetMapping("/findTeacher")
    public Result<PageResult<Teacher>> findTeacher(
            @Parameter(description = "页码，从1开始", example = "1")
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小", example = "10")
            @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "老师名（可选，支持模糊查询）", example = "张三")
            @RequestParam(required = false) String realName,
            @Parameter(description = "科目名称（可选，支持模糊查询）", example = "英语")
            @RequestParam(required = false) String subject) {


        PageResult<Teacher> result = teacherService.findTeacher(page, size, realName, subject);

        return Result.success(result);
    }

    @Operation(summary = "查找老师下的所有课程", description = "查询指定老师下的所有课程")
    @GetMapping("/findLessonByTeacherId")
    public Result<List<Lesson>> findCourseByTeacherId(@RequestParam Long teacherId) {
        List<Lesson> lessons = teacherService.findLessonByTeacherId(teacherId);
        return Result.success(lessons);
    }


}
