package com.example.rural_tutoring.util;

import lombok.Data;

@Data
public class WxLoginRequest {

    private String code;

    /**
     * 角色id
     */
    private Integer roleId;

}