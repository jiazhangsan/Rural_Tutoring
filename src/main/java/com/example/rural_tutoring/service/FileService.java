package com.example.rural_tutoring.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadFile(MultipartFile file, String directory);
    void deleteFile(String fileUrl);
    String getFileUrl(String objectName);
} 