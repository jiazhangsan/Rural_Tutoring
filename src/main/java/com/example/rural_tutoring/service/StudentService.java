package com.example.rural_tutoring.service;

import com.example.rural_tutoring.entity.Student;
import com.example.rural_tutoring.entity.TutoringNeed;

import java.util.List;

public interface StudentService {
    int addStudent(Student student);
    int deleteStudent(Integer id);
    int updateStudent(Student student);
    Student getStudent(Integer id);
    List<Student> getAllStudents();

    /**
     * 发布家教需求
     * @param tutoringNeed 需求信息
     * @return 操作结果
     */
    int publishNeed(TutoringNeed tutoringNeed);

    /**
     * 查询学生发布的所有需求
     * @param studentId 学生ID
     * @return 需求列表
     */
    List<TutoringNeed> findNeedsByStudentId(Integer studentId);
}