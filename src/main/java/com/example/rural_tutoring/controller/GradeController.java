package com.example.rural_tutoring.controller;

import com.example.rural_tutoring.config.Result;
import com.example.rural_tutoring.entity.Grade;
import com.example.rural_tutoring.model.PageResult;
import com.example.rural_tutoring.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jiashuhang
 * @description 年级管理控制器
 * @date 2025-01-XX
 **/
@Tag(name = "年级管理", description = "年级的增删改查")
@RestController
@RequestMapping("/api/grade")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @Operation(summary = "添加年级")
    @PostMapping("/add")
    public Result<Integer> addGrade(@RequestBody Grade grade) {
        try {
            int result = gradeService.addGrade(grade);
            if (result > 0) {
                return Result.success(result);
            } else {
                return Result.error("添加年级失败");
            }
        } catch (Exception e) {
            return Result.error("添加年级失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除年级")
    @DeleteMapping("/delete/{id}")
    public Result<Integer> deleteGrade(@Parameter(description = "年级ID") @PathVariable Integer id) {
        try {
            int result = gradeService.deleteGrade(id);
            if (result > 0) {
                return Result.success(result);
            } else {
                return Result.error("删除年级失败");
            }
        } catch (Exception e) {
            return Result.error("删除年级失败: " + e.getMessage());
        }
    }

    @Operation(summary = "更新年级")
    @PutMapping("/update")
    public Result<Integer> updateGrade(@RequestBody Grade grade) {
        try {
            int result = gradeService.updateGrade(grade);
            if (result > 0) {
                return Result.success(result);
            } else {
                return Result.error("更新年级失败");
            }
        } catch (Exception e) {
            return Result.error("更新年级失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据ID获取年级")
    @GetMapping("/get/{id}")
    public Result<Grade> getGrade(@Parameter(description = "年级ID") @PathVariable Integer id) {
        try {
            Grade grade = gradeService.getGrade(id);
            if (grade != null) {
                return Result.success(grade);
            } else {
                return Result.error("年级不存在");
            }
        } catch (Exception e) {
            return Result.error("获取年级失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取所有年级")
    @GetMapping("/list")
    public Result<List<Grade>> getAllGrades() {
        try {
            List<Grade> grades = gradeService.getAllGrades();
            return Result.success(grades);
        } catch (Exception e) {
            return Result.error("获取年级列表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "分页获取年级")
    @GetMapping("/page")
    public Result<PageResult<Grade>> getGradesByPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        try {
            PageResult<Grade> result = gradeService.getGradesByPage(page, size);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("分页获取年级失败: " + e.getMessage());
        }
    }
} 