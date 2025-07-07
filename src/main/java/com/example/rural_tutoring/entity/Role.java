package com.example.rural_tutoring.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 角色实体类
 * 
 * @author jiashuhang
 * @date 2025-01-07
 */
@Data
@Schema(description = "角色信息")
public class Role {
    
    @Schema(description = "角色ID", example = "1")
    private Integer id;
    
    @Schema(description = "角色名称", example = "管理员", allowableValues = {"管理员", "教师", "学生"})
    private String roleName; // "教师", "学生"
}
