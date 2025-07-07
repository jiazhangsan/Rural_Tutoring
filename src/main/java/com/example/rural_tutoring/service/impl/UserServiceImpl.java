package com.example.rural_tutoring.service.impl;

import com.example.rural_tutoring.entity.User;
import com.example.rural_tutoring.mapper.UserMapper;
import com.example.rural_tutoring.mapper.UserRoleMapper;
import com.example.rural_tutoring.model.DTO.UserDTO;
import com.example.rural_tutoring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    // 推荐将加密器声明为Bean并注入
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public int addUser(UserDTO userdto) {
        userdto.setCreatedAt(new Date());
        // 密码加密
        String encodedPwd = passwordEncoder.encode(userdto.getPasswordHash());
        userdto.setPasswordHash(encodedPwd);
        int i = userMapper.insertUser(userdto);
        if (i == 0) {
            return i;
        }

        Integer roleId = userdto.getRoleId();

        return userRoleMapper.insertUserRole(userdto.getId(), roleId);
    }

    @Override
    public int deleteUser(Integer id) {
        return userMapper.deleteUser(id);
    }

    @Override
    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }

    @Override
    public User getUserById(Integer id) {
        return userMapper.selectUserById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.selectAllUsers();
    }
}
