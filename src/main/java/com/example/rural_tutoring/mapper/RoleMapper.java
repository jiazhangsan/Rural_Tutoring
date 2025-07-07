package com.example.rural_tutoring.mapper;

import com.example.rural_tutoring.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {
    int insertRole(Role role);
    
    /**
     * 分页查询角色列表
     * @param offset 偏移量
     * @param limit 限制数量
     * @param name 角色名称（可选）
     * @return 角色列表
     */
    List<Role> selectRolesByPage(@Param("offset") int offset, 
                                 @Param("limit") int limit, 
                                 @Param("name") String name);
    
    /**
     * 查询角色总数
     * @param name 角色名称（可选）
     * @return 总数
     */
    long countRoles(@Param("name") String name);
    
    /**
     * 更新角色名称
     * @param role 角色信息
     * @return 更新影响的行数
     */
    int updateRoleName(Role role);
    
    /**
     * 删除角色
     * @param id 角色ID
     * @return 删除影响的行数
     */
    int deleteRole(@Param("id") Long id);
}
