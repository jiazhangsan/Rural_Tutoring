package com.example.rural_tutoring.mapper;

import com.example.rural_tutoring.entity.Role;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RoleMapper {
    int insertRole(Role role);
}
