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
    private Integer user_id; // 用户id
}
