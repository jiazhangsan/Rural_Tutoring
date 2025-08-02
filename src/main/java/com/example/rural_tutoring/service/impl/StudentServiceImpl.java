package com.example.rural_tutoring.service.impl;

import com.example.rural_tutoring.entity.Student;
import com.example.rural_tutoring.entity.TutoringNeed;
import com.example.rural_tutoring.mapper.StudentMapper;
import com.example.rural_tutoring.mapper.TutoringNeedMapper;
import com.example.rural_tutoring.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;
    
    @Autowired
    private TutoringNeedMapper tutoringNeedMapper;

    @Override
    public int addStudent(Student student) {
        return studentMapper.insert(student);
    }

    @Override
    public int deleteStudent(Integer id) {
        return studentMapper.deleteById(id);
    }

    @Override
    public int updateStudent(Student student) {
        return studentMapper.update(student);
    }

    @Override
    public Student getStudent(Integer id) {
        return studentMapper.selectById(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentMapper.selectAll();
    }

    @Override
    @Transactional
    public int publishNeed(TutoringNeed tutoringNeed) {
        // 验证学生是否存在
        Student student = studentMapper.selectById(tutoringNeed.getStudentId());
        if (student == null) {
            throw new RuntimeException("学生不存在");
        }
        
        // 发布需求
        return tutoringNeedMapper.insertTutoringNeed(tutoringNeed);
    }

    @Override
    public List<TutoringNeed> findNeedsByStudentId(Integer studentId) {
        return tutoringNeedMapper.findByStudentId(studentId);
    }

    @Override
    public Student getStudentByUserId(Integer userId) {
        return studentMapper.getStudentByUserId(userId);
    }
}