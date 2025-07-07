package com.example.rural_tutoring.controller;


import com.example.rural_tutoring.config.Result;
import com.example.rural_tutoring.entity.User;
import com.example.rural_tutoring.model.DTO.UserDTO;
import com.example.rural_tutoring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // 新增用户
    @PostMapping
    public Result<UserDTO> addUser(@RequestBody UserDTO userdto) {
        int i = userService.addUser(userdto);
        if (i > 0) {
            return Result.success(userdto);
        } else {
            return Result.error("添加失败");
        }
    }

    // 删除用户
    @DeleteMapping("/{id}")
    public Result<?> deleteUser(@PathVariable Integer id) {
        int i = userService.deleteUser(id);
        if (i > 0) {
            return Result.success(null);
        } else {
            return Result.error("用户不存在");
        }
    }

    // 修改用户
    @PutMapping
    public Result<User> updateUser(@RequestBody User user) {
        int i = userService.updateUser(user);
        if (i > 0) {
            return Result.success(user);
        } else {
            return Result.error("用户不存在或更新失败");
        }
    }

    // 查询单个用户
    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error("用户不存在");
        }
    }

    // 查询所有用户
    @GetMapping
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Result.success(users);
    }
}
