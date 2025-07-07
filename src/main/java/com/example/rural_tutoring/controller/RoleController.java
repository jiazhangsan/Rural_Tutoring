package com.example.rural_tutoring.controller;

import com.example.rural_tutoring.config.Result;
import com.example.rural_tutoring.entity.Role;
import com.example.rural_tutoring.model.PageResult;
import com.example.rural_tutoring.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 角色管理控制器
 * 
 * @author jiashuhang
 * @date 2025-01-07
 */
@Tag(name = "角色管理", description = "角色相关的CRUD操作")
@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;
    
    /**
     * 新增角色
     */
    @Operation(summary = "新增角色", description = "创建一个新的角色")
    @PostMapping
    public Result<Role> addRole(@RequestBody Role role) {
        int i = roleService.addRole(role);
        if (i > 0) {
            return Result.success(role);
        } else {
            return Result.error("添加失败");
        }
    }

    /**
     * 分页查询角色
     */
    @Operation(summary = "分页查询角色", description = "分页查询所有角色，支持模糊查询")
    @GetMapping
    public Result<PageResult<Role>> getAllRoles(
            @Parameter(description = "页码，从1开始", example = "1") 
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小", example = "10") 
            @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "角色名称（可选，支持模糊查询）", example = "管理员") 
            @RequestParam(required = false) String name) {
        
        // 参数验证
        if (page < 1) page = 1;
        if (size < 1 || size > 100) size = 10;
        
        PageResult<Role> result = roleService.getRolesByPage(page, size, name);
        return Result.success(result);
    }

    /**
     * 更新角色名称
     */
    @Operation(summary = "更新角色名称", description = "根据角色ID更新角色名称")
    @PutMapping
    public Result<Role> updateRoleName(@RequestBody Role role) {
        // 参数验证
        if (role == null || role.getId() == null) {
            return Result.error("角色ID不能为空");
        }
        if (role.getRoleName() == null || role.getRoleName().trim().isEmpty()) {
            return Result.error("角色名称不能为空");
        }
        
        int i = roleService.updateRoleName(role);
        if (i > 0) {
            return Result.success(role);
        } else {
            return Result.error("更新失败，角色可能不存在");
        }
    }

    /**
     * 删除角色
     */
    @Operation(summary = "删除角色", description = "根据角色ID删除角色")
    @DeleteMapping("/{id}")
    public Result<Void> deleteRole(@Parameter(description = "角色ID", example = "1") @PathVariable Long id) {
        // 参数验证
        if (id == null || id <= 0) {
            return Result.error("角色ID不能为空且必须大于0");
        }
        
        int i = roleService.deleteRole(id);
        if (i > 0) {
            return Result.success(null);
        } else {
            return Result.error("删除失败，角色可能不存在");
        }
    }
}
