package com.example.rural_tutoring.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileService {
    /**
     * 上传文件
     * @param file 文件
     * @param directory 存储目录
     * @return 文件访问URL
     */
    String uploadFile(MultipartFile file, String directory) throws IOException;
    
    /**
     * 删除文件
     * @param fileUrl 文件URL
     */
    void deleteFile(String fileUrl) throws IOException;
} 