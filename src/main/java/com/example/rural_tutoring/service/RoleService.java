package com.example.rural_tutoring.service;

import com.example.rural_tutoring.entity.Role;
import com.example.rural_tutoring.model.PageResult;

public interface RoleService {
    int addRole(Role role);
    
    /**
     * 分页查询角色
     * @param page 页码
     * @param size 每页大小
     * @param name 角色名称（可选）
     * @return 分页结果
     */
    PageResult<Role> getRolesByPage(Integer page, Integer size, String name);
    
    /**
     * 更新角色名称
     * @param role 角色信息（包含id和新的roleName）
     * @return 更新影响的行数
     */
    int updateRoleName(Role role);
    
    /**
     * 删除角色
     * @param id 角色ID
     * @return 删除影响的行数
     */
    int deleteRole(Long id);
}
