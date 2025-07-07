package com.example.rural_tutoring.service.impl;

import com.example.rural_tutoring.entity.Role;
import com.example.rural_tutoring.mapper.RoleMapper;
import com.example.rural_tutoring.model.PageResult;
import com.example.rural_tutoring.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    
    /**
     * @author jiashuhang
     * @description 分页查询角色
     * @param page 页码
     * @param size 每页大小
     * @param name 角色名称（可选）
     * @return 分页结果
     * @date 2025-07-07 09:10
     **/
    @Override
    public PageResult<Role> getRolesByPage(Integer page, Integer size, String name) {
        // 计算偏移量
        int offset = (page - 1) * size;
        
        // 查询数据
        List<Role> roles = roleMapper.selectRolesByPage(offset, size, name);
        
        // 查询总数
        long total = roleMapper.countRoles(name);
        
        // 返回分页结果
        return new PageResult<>(roles, total, page, size);
    }
    
    /**
     * @author jiashuhang
     * @description 更新角色名称
     * @param role 角色信息（包含id和新的roleName）
     * @return 更新影响的行数
     * @date 2025-07-07 09:46
     **/
    @Override
    public int updateRoleName(Role role) {
        return roleMapper.updateRoleName(role);
    }
    
    /**
     * @author jiashuhang
     * @description 删除角色
     * @param id 角色ID
     * @return 删除影响的行数
     * @date 2025-07-07 09:50
     **/
    @Override
    public int deleteRole(Long id) {
        return roleMapper.deleteRole(id);
    }
}
