package com.example.rural_tutoring.controller;

import com.example.rural_tutoring.config.Result;
import com.example.rural_tutoring.entity.User;
import com.example.rural_tutoring.model.DTO.UserDTO;
import com.example.rural_tutoring.model.PageResult;
import com.example.rural_tutoring.service.UserService;
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
     * @date 2025-07-16 10:15
     **/
    @PostMapping("/wxLogin")
    public Result<UserDTO> wxLogin(@RequestBody WxLoginRequest request) {
        String code = request.getCode();
        if (code == null || code.isEmpty()) {
            return Result.error("code不能为空");
        }
        // 1. 请求微信服务器获取openid和session_key
        String url = String.format(
            "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
            wxAppId, wxSecret, code
        );
        RestTemplate restTemplate = new RestTemplate();
        WxSessionResponse wxResp = restTemplate.getForObject(url, WxSessionResponse.class);
        if (wxResp == null || wxResp.getOpenid() == null) {
            return Result.error("微信登录失败");
        }
        // 2. 查找或注册用户
        UserDTO userdto = userService.wxLoginOrRegister(wxResp.getOpenid(), request.getRoleId());
        // 3. 生成token（可用JWT）
        String token = jwtUtil.generateToken(userdto);
        userdto.setToken(token);
        return Result.success(userdto);
    }


}
