package com.example.rural_tutoring.controller;

import com.example.rural_tutoring.config.Result;
import com.example.rural_tutoring.entity.Student;
import com.example.rural_tutoring.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 学生管理控制器
 * 
 * @author jiashuhang
 * @date 2025-01-07
 */
@Tag(name = "学生管理", description = "学生相关的CRUD操作")
@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 新增学生
     */
    @Operation(summary = "新增学生", description = "创建一个新的学生")
    @PostMapping("/addStudent")
    public Result<Student> addStudent(@RequestBody Student student) {
        int i = studentService.addStudent(student);
        if (i > 0) {
            return Result.success(student);
        } else {
            return Result.error("添加失败");
        }
    }

    /**
     * 删除学生
     */
    @Operation(summary = "删除学生", description = "根据学生ID删除学生")
    @DeleteMapping("/{id}")
    public int deleteStudent(@Parameter(description = "学生ID", example = "S001") @PathVariable String id) {
        return studentService.deleteStudent(id);
    }

    /**
     * 修改学生
     */
    @Operation(summary = "修改学生", description = "更新学生信息")
    @PutMapping
    public int updateStudent(@RequestBody Student student) {
        return studentService.updateStudent(student);
    }

    /**
     * 查询单个学生
     */
    @Operation(summary = "查询单个学生", description = "根据学生ID查询学生详情")
    @GetMapping("/{id}")
    public Student getStudent(@Parameter(description = "学生ID", example = "S001") @PathVariable String id) {
        return studentService.getStudent(id);
    }

    /**
     * 查询所有学生
     */
    @Operation(summary = "查询所有学生", description = "获取所有学生列表")
    @GetMapping
    public Result<List<Student>> getAllStudents() {
        List<Student> allStudents = studentService.getAllStudents();
        if (!allStudents.isEmpty()) {
            return Result.success(allStudents);
        } else {
            return Result.error("获取所有学生数据失败！");
        }
    }
}