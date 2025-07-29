package com.example.rural_tutoring.service.impl;

import com.example.rural_tutoring.entity.Subject;
import com.example.rural_tutoring.mapper.SubjectMapper;
import com.example.rural_tutoring.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    private SubjectMapper subjectMapper;

    @Override
    public boolean createSubject(Subject subject) {
        return subjectMapper.insertSubject(subject) > 0;
    }
} 