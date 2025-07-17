//package com.example.rural_tutoring.service.impl;
//
//import com.example.rural_tutoring.service.FileService;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.UUID;
//
//@Service
//public class FileServiceImpl implements FileService {
//
//    @Value("${file.upload.path}")
//    private String uploadPath;
//
//    @Value("${file.access.url}")
//    private String accessUrl;
//
//    @Override
//    public String uploadFile(MultipartFile file, String directory) throws IOException {
//        // 1. 获取文件名和扩展名
//        String originalFilename = file.getOriginalFilename();
//        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
//
//        // 2. 生成新的文件名（使用UUID和时间戳）
//        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
//        String newFilename = UUID.randomUUID().toString() + "_" + timestamp + extension;
//
//        // 3. 创建目标目录
//        String targetDir = uploadPath + File.separator + directory;
//        Path targetPath = Paths.get(targetDir);
//        if (!Files.exists(targetPath)) {
//            Files.createDirectories(targetPath);
//        }
//
//        // 4. 保存文件
//        Path filePath = targetPath.resolve(newFilename);
//        Files.copy(file.getInputStream(), filePath);
//
//        // 5. 返回文件访问URL
//        return accessUrl + "/" + directory + "/" + newFilename;
//    }
//
//    @Override
//    public void deleteFile(String fileUrl) throws IOException {
//        // 1. 从URL中提取文件路径
//        String relativePath = fileUrl.substring(accessUrl.length());
//        Path filePath = Paths.get(uploadPath, relativePath);
//
//        // 2. 删除文件
//        Files.deleteIfExists(filePath);
//    }
//}