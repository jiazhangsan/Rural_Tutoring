package com.example.rural_tutoring.mapper;

import com.example.rural_tutoring.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    int insertUser(User user);

    int deleteUser(Integer id);
    int updateUser(User user);
    User selectUserById(Integer id);
    List<User> selectAllUsers();
    List<User> selectUsersByPage(@Param("offset") int offset, @Param("limit") int limit, @Param("username") String username);
    long countUsers(@Param("username") String username);
}
