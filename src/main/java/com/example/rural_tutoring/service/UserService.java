package com.example.rural_tutoring.service;

import com.example.rural_tutoring.entity.User;
import com.example.rural_tutoring.model.DTO.UserDTO;
import com.example.rural_tutoring.model.PageResult;

import java.util.List;

public interface UserService {
    int addUser(UserDTO userdto);
    int deleteUser(Integer id);
    int updateUser(User user);
    User getUserById(Integer id);
    List<User> getAllUsers();
    PageResult<User> getUsersByPage(Integer page, Integer size, String username);
    UserDTO wxLoginOrRegister(String openid, Integer roleId);
} 