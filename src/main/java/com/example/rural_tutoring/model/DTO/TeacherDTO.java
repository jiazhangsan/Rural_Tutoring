package com.example.rural_tutoring.model.DTO;

import com.example.rural_tutoring.entity.Teacher;
import lombok.Data;

@Data
public class TeacherDTO extends Teacher {
    /**
     * 科目的id
     */
    private Integer subjectId;
}
