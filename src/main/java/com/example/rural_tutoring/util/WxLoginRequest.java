package com.example.rural_tutoring.util;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class WxLoginRequest {

    private String code;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 昵称
     */
    private String username;

    /**
     * 手机号
     */
    private String phoneNumber;
    
    /**
     * 头像图片文件
     */
    private MultipartFile avatarFile;
    
    /**
     * 年级
     */
    private Integer gradeId;
    
    /**
     * 老师验证码（用于老师身份验证）
     */
    private String teacherVerificationCode;


    /**
     * 科目id
     */
    private Integer subjectId;
    

}