package com.example.rural_tutoring.entity;

import lombok.Data;

/**
 * @author jiashuhang
 * @description 老师信息认证
 * @date 2025-07-04 19:06
 **/
@Data
public class Teacher {
    private Integer id;        // id
    private String realName;       // 真实姓名（用于家长验证）
    private String university;     // 毕业院校
    private String major;          // 专业（如"数学系"）
    private Integer userId; // 用户id
    
    // 地理位置相关字段
    private Double latitude;       // 纬度
    private Double longitude;      // 经度
    private String address;        // 详细地址
    private String city;           // 城市
    private String district;       // 区域/区县
    private String province;       // 省份

    private String teacherVerificationCode; // 老师验证码

}
