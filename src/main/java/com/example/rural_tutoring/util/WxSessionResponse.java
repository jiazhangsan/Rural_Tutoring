package com.example.rural_tutoring.util;


import lombok.Data;

@Data
public class WxSessionResponse {
        private String openid;
        private String session_key;
        private String unionid;
        private Integer errcode;
        private String errmsg;
    }