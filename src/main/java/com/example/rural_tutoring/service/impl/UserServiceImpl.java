package com.example.rural_tutoring.service.impl;

import com.example.rural_tutoring.entity.User;
import com.example.rural_tutoring.mapper.UserMapper;
import com.example.rural_tutoring.mapper.UserRoleMapper;
import com.example.rural_tutoring.model.DTO.UserDTO;
import com.example.rural_tutoring.model.PageResult;
import com.example.rural_tutoring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
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

    @Override
    public PageResult<User> getUsersByPage(Integer page, Integer size, String username) {
        int offset = (page - 1) * size;
        List<User> users = userMapper.selectUsersByPage(offset, size, username);
        long total = userMapper.countUsers(username);
        return new PageResult<>(users, total, page, size);
    }

    @Override
    public UserDTO wxLoginOrRegister(String openid, Integer roleId) {
        User user = userMapper.selectByOpenid(openid);
        if (user == null) {
            user = new User();
            user.setOpenid(openid);
            user.setUsername("wx_" + openid.substring(0, 8));

            LocalDateTime now = LocalDateTime.now();
            Date createdAtDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
            user.setCreatedAt(createdAtDate);

            userMapper.weChatRegistration(user);


            userRoleMapper.insertUserRole(user.getId(), roleId);
        }
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setOpenid(user.getOpenid());
        return dto;
    }
}
