package com.example.rural_tutoring.controller;

import com.example.rural_tutoring.config.Result;
import com.example.rural_tutoring.entity.Teacher;
import com.example.rural_tutoring.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "教师管理", description = "教师相关的CRUD操作")
@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Operation(summary = "新增老师", description = "创建一个新的老师")
    @PostMapping
    public Result<Teacher> addTeacher(@RequestBody Teacher teacher) {
        int i = teacherService.addTeacher(teacher);
        if (i > 0) {
            return Result.success(teacher);
        } else {
            return Result.error("添加失败");
        }
    }
}
