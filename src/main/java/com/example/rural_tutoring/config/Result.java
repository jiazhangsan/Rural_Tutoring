package com.example.rural_tutoring.config;

import lombok.Data;

@Data
public class Result<T> {
    private int code;      // 状态码（如200、400、500等）
    private String msg;    // 提示信息
    private T data;        // 具体数据

    // 静态工厂方法
    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMsg("success");
        r.setData(data);
        return r;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> r = new Result<>();
        r.setCode(500);
        r.setMsg(msg);
        r.setData(null);
        return r;
    }
}