package com.example.rural_tutoring.controller;

import com.example.rural_tutoring.config.Result;
import com.example.rural_tutoring.entity.Subject;
import com.example.rural_tutoring.service.SubjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "科目管理", description = "科目相关的CRUD操作")
@RestController
@RequestMapping("/api/subject")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @Operation(summary = "创建科目", description = "添加新的科目")
    @PostMapping("/create")
    public Result<Subject> createSubject(@RequestBody Subject subject) {
        boolean success = subjectService.createSubject(subject);
        if (success) {
            return Result.success(subject);
        } else {
            return Result.error("创建科目失败");
        }
    }
} 