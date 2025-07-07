package com.example.rural_tutoring.controller;


import com.example.rural_tutoring.config.Result;
import com.example.rural_tutoring.entity.Student;
import com.example.rural_tutoring.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/addStudent")
    public Result<Student> addStudent(@RequestBody Student student) {
        int i = studentService.addStudent(student);
        if (i > 0) {
            return Result.success(student);
        } else {
            return Result.error("添加失败");
        }

    }

    @DeleteMapping("/{id}")
    public int deleteStudent(@PathVariable String id) {
        return studentService.deleteStudent(id);
    }

    @PutMapping
    public int updateStudent(@RequestBody Student student) {
        return studentService.updateStudent(student);
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable String id) {
        return studentService.getStudent(id);
    }

    @GetMapping
    public Result<List<Student>> getAllStudents() {
        List<Student> allStudents = studentService.getAllStudents();
        if (!allStudents.isEmpty()) {
            return Result.success(allStudents);
        }else {
            return Result.error("获取所有学生数据失败！");
        }

    }
}