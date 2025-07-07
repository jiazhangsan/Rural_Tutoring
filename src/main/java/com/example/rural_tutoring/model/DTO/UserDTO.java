package com.example.rural_tutoring.model.DTO;

import com.example.rural_tutoring.entity.User;
import lombok.Data;

@Data
public class UserDTO extends User {
    /**
     * 角色id
     */
    private Integer roleId;
}
