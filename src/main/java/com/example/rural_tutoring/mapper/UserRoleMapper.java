package com.example.rural_tutoring.mapper;


import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper {
    int insertUserRole(Integer userId, Integer roleId);
}
