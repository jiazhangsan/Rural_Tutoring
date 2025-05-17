package com.example.rural_tutoring.service;

import com.example.rural_tutoring.entity.Student;

import java.util.List;

public interface StudentService {
    int addStudent(Student student);
    int deleteStudent(String id);
    int updateStudent(Student student);
    Student getStudent(String id);
    List<Student> getAllStudents();
}