package com.example.rural_tutoring.service;

import com.example.rural_tutoring.entity.User;
import com.example.rural_tutoring.model.DTO.UserDTO;
import com.example.rural_tutoring.model.PageResult;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.util.List;

public interface UserService {
    int addUser(UserDTO userdto);
    int deleteUser(Integer id);
    int updateUser(User user);
    User getUserById(Integer id);
    List<User> getAllUsers();
    PageResult<User> getUsersByPage(Integer page, Integer size, String username);
    UserDTO wxLoginOrRegister(String openid, Integer roleId);
    
    /**
     * 微信登录或注册为学生
     */
    UserDTO wxLoginOrRegisterAsStudent(String openid, String username, String phoneNumber, String avatarUrl, Integer grade, Integer roleId);
    
    /**
     * 微信登录或注册为教师
     */
    UserDTO wxLoginOrRegisterAsTeacher(String openid, String username, String phoneNumber, String avatarUrl, String teacherVerificationCode, Integer roleId, Integer subjectId);
} 