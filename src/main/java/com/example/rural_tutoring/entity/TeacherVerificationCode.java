package com.example.rural_tutoring.entity;

import lombok.Data;
import java.util.Date;

/**
 * @author jiashuhang
 * @description 老师验证码实体
 * @date 2025-01-XX
 **/
@Data
public class TeacherVerificationCode {
    private Integer id;                    // 验证码ID
    private String code;                   // 验证码
    private String description;            // 验证码描述
    private Boolean isActive;              // 是否启用
    private Integer maxUsage;              // 最大使用次数（-1表示无限制）
    private Integer usedCount;             // 已使用次数
    private Integer createdBy;             // 创建人ID（管理员）
    private Date createdAt;                // 创建时间
    private Date updatedAt;                // 更新时间
} 