package com.example.rural_tutoring.service.impl;

import com.example.rural_tutoring.entity.Role;
import com.example.rural_tutoring.mapper.RoleMapper;
import com.example.rural_tutoring.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {


    @Autowired
    private RoleMapper roleMapper;
    /**
     * @author jiashuhang
     * @description 添加角色
     * @date 2025-07-04 18:09
     **/
    @Override
    public int addRole(Role role) {

        return roleMapper.insertRole(role);

    }
}
