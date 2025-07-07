package com.example.rural_tutoring.controller;

import com.example.rural_tutoring.config.Result;
import com.example.rural_tutoring.entity.Role;

import com.example.rural_tutoring.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;
    // 新增角色
    @PostMapping
    public Result<Role> addRole(@RequestBody Role role) {
        int i = roleService.addRole(role);
        if (i > 0) {
            return Result.success(role);
        } else {
            return Result.error("添加失败");
        }
    }
}
