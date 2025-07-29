package com.example.rural_tutoring.service.impl;

import com.example.rural_tutoring.entity.User;
import com.example.rural_tutoring.entity.Student;
import com.example.rural_tutoring.entity.Teacher;
import com.example.rural_tutoring.mapper.UserMapper;
import com.example.rural_tutoring.mapper.UserRoleMapper;
import com.example.rural_tutoring.model.DTO.UserDTO;
import com.example.rural_tutoring.model.DTO.TeacherDTO;
import com.example.rural_tutoring.model.PageResult;
import com.example.rural_tutoring.service.UserService;
import com.example.rural_tutoring.service.StudentService;
import com.example.rural_tutoring.service.TeacherService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

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
            // 新用户注册
            user = new User();
            user.setOpenid(openid);
            System.out.println("user下的openid:" + user.getOpenid());
            user.setUsername("wx_" + openid.substring(0, 8));
            user.setAccount("wx_" + openid.substring(0, 8)); // 设置账号
            user.setPasswordHash(""); // 微信用户不需要密码


            // 设置创建时间
            Date createdAtDate = new Date();
            user.setCreatedAt(createdAtDate);

            // 插入用户
            userMapper.weChatRegistration(user);

            // 插入用户角色关联
            if (roleId != null) {
                userRoleMapper.insertUserRole(user.getId(), roleId);
            }

            // 构建返回DTO
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setOpenid(user.getOpenid());
            dto.setAccount(user.getAccount());
            dto.setRoleId(roleId);
            return dto;
        } else {
            // 老用户登录
            UserDTO dto = new UserDTO();
            dto.setId(user.getId());
            dto.setUsername(user.getUsername());
            dto.setOpenid(user.getOpenid());
            dto.setAccount(user.getAccount());
            return dto;
        }
    }

    @Override
    @Transactional
    public UserDTO wxLoginOrRegisterAsStudent(String openid, String username, String phoneNumber, String avatarUrl, Integer gradeId, Integer roleId) {
        System.out.println("微信登录/注册为学生，openid: " + openid);
        User user = userMapper.selectByOpenid(openid);
        
        if (user == null) {
            // 新用户注册
            user = new User();
            user.setOpenid(openid);
            if (username == null) {
                user.setUsername("wx_" + openid.substring(0, 8));
            }else{
                user.setUsername(username);
            }
            user.setAccount("wx_" + openid.substring(0, 8));
            user.setPasswordHash(""); // 微信用户不需要密码
            user.setCreatedAt(new Date());
            user.setAvatarUrl(avatarUrl);
            user.setPhoneNumber(phoneNumber);

            // 插入用户
            userMapper.weChatRegistration(user);


            userRoleMapper.insertUserRole(user.getId(), roleId);

            // 创建学生信息
            Student student = new Student();
            student.setName(username != null ? username : "学生");
            student.setGradeId(gradeId);

            student.setUserId(user.getId());
            
            // 插入学生信息
            studentService.addStudent(student);

            System.out.println("新学生注册成功，用户ID: " + user.getId() + ", 学生ID: " + student.getId());
        } else {
            System.out.println("学生登录成功，用户ID: " + user.getId());
        }

        // 构建返回DTO
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setOpenid(user.getOpenid());
        dto.setAccount(user.getAccount());
        dto.setRoleId(roleId); // 学生角色ID
        dto.setPhoneNumber(phoneNumber);
        dto.setAvatarUrl(avatarUrl);
        return dto;
    }

    @Override
    @Transactional
    public UserDTO wxLoginOrRegisterAsTeacher(String openid, String username, String phoneNumber, String avatarUrl, String teacherVerificationCode, Integer roleId, Integer subjectId) {
        System.out.println("微信登录/注册为教师，openid: " + openid);
        User user = userMapper.selectByOpenid(openid);
        
        if (user == null) {
            // 新用户注册
            user = new User();
            user.setOpenid(openid);
            if (username == null) {
                user.setUsername("wx_" + openid.substring(0, 8));
            }else{
                user.setUsername(username);
            }

            user.setAccount("wx_" + openid.substring(0, 8));
            user.setPasswordHash(""); // 微信用户不需要密码
            user.setCreatedAt(new Date());
            user.setAvatarUrl(avatarUrl);
            user.setPhoneNumber(phoneNumber);
            // 插入用户
            userMapper.weChatRegistration(user);

            userRoleMapper.insertUserRole(user.getId(), roleId);

            // 创建教师信息
            TeacherDTO teacherDTO = new TeacherDTO();
            teacherDTO.setRealName(username != null ? username : "教师");
            teacherDTO.setUserId(user.getId());
            teacherDTO.setTeacherVerificationCode(teacherVerificationCode);
            teacherDTO.setSubjectId(user.getId());
            teacherDTO.setSubjectId(subjectId);

            // 可以根据需要设置其他教师信息
            
            // 插入教师信息
            teacherService.addTeacher(teacherDTO);

            System.out.println("新教师注册成功，用户ID: " + user.getId());
        } else {
            System.out.println("教师登录成功，用户ID: " + user.getId());
        }

        // 构建返回DTO
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setOpenid(user.getOpenid());
        dto.setAccount(user.getAccount());
        dto.setRoleId(roleId); // 教师角色ID
        dto.setPhoneNumber(phoneNumber);
        dto.setAvatarUrl(avatarUrl);
        return dto;
    }
}
