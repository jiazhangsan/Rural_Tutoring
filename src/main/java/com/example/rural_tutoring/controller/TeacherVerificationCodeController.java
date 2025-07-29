package com.example.rural_tutoring.controller;

import com.example.rural_tutoring.config.Result;
import com.example.rural_tutoring.entity.TeacherVerificationCode;
import com.example.rural_tutoring.model.PageResult;
import com.example.rural_tutoring.service.TeacherVerificationCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author jiashuhang
 * @description 老师验证码管理控制器
 * @date 2025-01-XX
 **/
@Tag(name = "老师验证码管理", description = "管理员生成和管理老师验证码")
@RestController
@RequestMapping("/api/teacher-verification-code")
public class TeacherVerificationCodeController {

    @Autowired
    private TeacherVerificationCodeService verificationCodeService;

    @Operation(summary = "生成验证码")
    @PostMapping("/generate")
    public Result<TeacherVerificationCode> generateVerificationCode(
            @Parameter(description = "验证码描述") @RequestParam String description,
            @Parameter(description = "最大使用次数（-1表示无限制）") @RequestParam(defaultValue = "-1") Integer maxUsage,
            @Parameter(description = "创建人ID") @RequestParam Integer createdBy) {
        try {
            TeacherVerificationCode code = verificationCodeService.generateVerificationCode(description, maxUsage, createdBy);
            if (code != null) {
                return Result.success(code);
            } else {
                return Result.error("生成验证码失败");
            }
        } catch (Exception e) {
            return Result.error("生成验证码失败: " + e.getMessage());
        }
    }

    @Operation(summary = "添加验证码")
    @PostMapping("/add")
    public Result<Integer> addVerificationCode(@RequestBody TeacherVerificationCode code) {
        try {
            int result = verificationCodeService.addVerificationCode(code);
            if (result > 0) {
                return Result.success(result);
            } else {
                return Result.error("添加验证码失败");
            }
        } catch (Exception e) {
            return Result.error("添加验证码失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除验证码")
    @DeleteMapping("/delete/{id}")
    public Result<Integer> deleteVerificationCode(@Parameter(description = "验证码ID") @PathVariable Integer id) {
        try {
            int result = verificationCodeService.deleteVerificationCode(id);
            if (result > 0) {
                return Result.success(result);
            } else {
                return Result.error("删除验证码失败");
            }
        } catch (Exception e) {
            return Result.error("删除验证码失败: " + e.getMessage());
        }
    }

    @Operation(summary = "更新验证码")
    @PutMapping("/update")
    public Result<Integer> updateVerificationCode(@RequestBody TeacherVerificationCode code) {
        try {
            int result = verificationCodeService.updateVerificationCode(code);
            if (result > 0) {
                return Result.success(result);
            } else {
                return Result.error("更新验证码失败");
            }
        } catch (Exception e) {
            return Result.error("更新验证码失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据ID获取验证码")
    @GetMapping("/get/{id}")
    public Result<TeacherVerificationCode> getVerificationCode(@Parameter(description = "验证码ID") @PathVariable Integer id) {
        try {
            TeacherVerificationCode code = verificationCodeService.getVerificationCode(id);
            if (code != null) {
                return Result.success(code);
            } else {
                return Result.error("验证码不存在");
            }
        } catch (Exception e) {
            return Result.error("获取验证码失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取所有验证码")
    @GetMapping("/list")
    public Result<List<TeacherVerificationCode>> getAllVerificationCodes() {
        try {
            List<TeacherVerificationCode> codes = verificationCodeService.getAllVerificationCodes();
            return Result.success(codes);
        } catch (Exception e) {
            return Result.error("获取验证码列表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "分页获取验证码")
    @GetMapping("/page")
    public Result<PageResult<TeacherVerificationCode>> getVerificationCodesByPage(
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        try {
            PageResult<TeacherVerificationCode> result = verificationCodeService.getVerificationCodesByPage(page, size);
            return Result.success(result);
        } catch (Exception e) {
            return Result.error("分页获取验证码失败: " + e.getMessage());
        }
    }

    @Operation(summary = "验证验证码")
    @PostMapping("/validate")
    public Result<Boolean> validateVerificationCode(@Parameter(description = "验证码") @RequestParam String code) {
        try {
            boolean isValid = verificationCodeService.validateVerificationCode(code);
            return Result.success(isValid);
        } catch (Exception e) {
            return Result.error("验证验证码失败: " + e.getMessage());
        }
    }
} 