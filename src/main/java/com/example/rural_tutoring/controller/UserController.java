package com.example.rural_tutoring.controller;

import com.example.rural_tutoring.config.Result;
import com.example.rural_tutoring.entity.User;
import com.example.rural_tutoring.model.DTO.UserDTO;
import com.example.rural_tutoring.model.PageResult;
import com.example.rural_tutoring.service.FrontendImageService;
import com.example.rural_tutoring.service.UserService;
import com.example.rural_tutoring.service.TeacherVerificationCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.example.rural_tutoring.util.WxLoginRequest;
import com.example.rural_tutoring.util.WxSessionResponse;
import com.example.rural_tutoring.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 用户管理控制器
 * 
 * @author jiashuhang
 * @date 2025-01-07
 */
@Tag(name = "用户管理", description = "用户相关的CRUD操作")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private FrontendImageService frontendImageService;

    @Autowired
    private TeacherVerificationCodeService verificationCodeService;

    @Value("${wx.appid}")
    private String wxAppId;

    @Value("${wx.secret}")
    private String wxSecret;

    /**
     * 新增用户
     */
    @Operation(summary = "新增用户", description = "创建一个新的用户")
    @PostMapping
    public Result<UserDTO> addUser(@RequestBody UserDTO userdto) {
        int i = userService.addUser(userdto);
        if (i > 0) {
            return Result.success(userdto);
        } else {
            return Result.error("添加失败");
        }
    }

    /**
     * 删除用户
     */
    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    @DeleteMapping("/{id}")
    public Result<?> deleteUser(@Parameter(description = "用户ID", example = "1") @PathVariable Integer id) {
        int i = userService.deleteUser(id);
        if (i > 0) {
            return Result.success(null);
        } else {
            return Result.error("用户不存在");
        }
    }

    /**
     * 修改用户
     */
    @Operation(summary = "修改用户", description = "更新用户信息")
    @PutMapping
    public Result<User> updateUser(@RequestBody User user) {
        int i = userService.updateUser(user);
        if (i > 0) {
            return Result.success(user);
        } else {
            return Result.error("用户不存在或更新失败");
        }
    }

    /**
     * 查询单个用户
     */
    @Operation(summary = "查询单个用户", description = "根据用户ID查询用户详情")
    @GetMapping("/{id}")
    public Result<User> getUser(@Parameter(description = "用户ID", example = "1") @PathVariable Integer id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error("用户不存在");
        }
    }

    /**
     * 查询所有用户
     */
    @Operation(summary = "分页查询用户", description = "分页查询所有用户，支持用户名模糊查询")
    @GetMapping
    public Result<PageResult<User>> getAllUsers(
            @Parameter(description = "页码，从1开始", example = "1")
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页大小", example = "10")
            @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "用户名（可选，支持模糊查询）", example = "张三")
            @RequestParam(required = false) String username) {

        if (page < 1) page = 1;
        if (size < 1 || size > 100) size = 10;

        PageResult<User> result = userService.getUsersByPage(page, size, username);
        return Result.success(result);
    }

    /**
     * @author jiashuhang
     * @description 微信注册登录账号
     * 1、注册分为两种情况，第一种是学生注册，第二种是老师注册，老师注册要有老师验证码
     * 2、在注册的时候要操作的表有学生表，用户表。和老师表和用户表
     *
     * @date 2025-07-16 10:15
     **/
    @PostMapping("/wxLogin")
    public Result<UserDTO> wxLogin(@ModelAttribute WxLoginRequest request) {
        String code = request.getCode();
        if (code == null || code.isEmpty()) {
            return Result.error("code不能为空");
        }
        
        // 验证老师验证码（如果提供的话）
        String teacherVerificationCode = request.getTeacherVerificationCode();
        if (teacherVerificationCode != null && !teacherVerificationCode.isEmpty()) {
            if (!verificationCodeService.validateVerificationCode(teacherVerificationCode)) {
                return Result.error("老师验证码无效或已过期");
            }
        }
        
        // 1. 请求微信服务器获取openid和session_key
        String url = String.format(
            "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
            wxAppId, wxSecret, code
        );
        RestTemplate restTemplate = new RestTemplate();
        
        try {
            // 先获取字符串响应
            String responseStr = restTemplate.getForObject(url, String.class);
            System.out.println("微信响应：" + responseStr);
            
            // 手动解析JSON
            ObjectMapper objectMapper = new ObjectMapper();
            WxSessionResponse wxResp = objectMapper.readValue(responseStr, WxSessionResponse.class);
            
            if (wxResp == null || wxResp.getOpenid() == null) {
                return Result.error("微信登录失败");
            }
            
            // 处理头像文件上传
            String avatarUrl = null;
            if (request.getAvatarFile() != null && !request.getAvatarFile().isEmpty()) {
                try {
                    // 这里可以调用文件上传服务
                     avatarUrl = frontendImageService.uploadFrontendImage(request.getAvatarFile(), "avatar");
                    // 暂时使用简单的方式
//                    avatarUrl = "/uploads/avatar/" + request.getAvatarFile().getOriginalFilename();
                } catch (Exception e) {
                    System.out.println("头像上传失败: " + e.getMessage());
                }
            }
            
            // 2. 根据teacherVerificationCode决定注册类型
            UserDTO userdto;
            if (teacherVerificationCode != null && !teacherVerificationCode.isEmpty()) {
                // 注册为教师
                userdto = userService.wxLoginOrRegisterAsTeacher(

                    wxResp.getOpenid(), 
                    request.getUsername(),
                    request.getPhoneNumber(),
                    avatarUrl,
                    teacherVerificationCode,
                        request.getRoleId(),
                        request.getSubjectId()
                );
                // 使用验证码（增加使用次数）
                verificationCodeService.useVerificationCode(teacherVerificationCode);
            } else {
                // 注册为学生
                userdto = userService.wxLoginOrRegisterAsStudent(
                    wxResp.getOpenid(),
                    request.getUsername(),
                    request.getPhoneNumber(),
                    avatarUrl,
                    request.getGradeId(),
                        request.getRoleId()
                );
            }

            userdto.setSession_key(wxResp.getSession_key());

            // 3. 生成token（可用JWT）
            String token = jwtUtil.generateToken(userdto);
            userdto.setToken(token);
            return Result.success(userdto);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("微信登录失败：" + e.getMessage());
        }
    }


}
