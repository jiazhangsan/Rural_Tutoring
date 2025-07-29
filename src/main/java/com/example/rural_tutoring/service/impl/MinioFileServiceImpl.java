package com.example.rural_tutoring.service.impl;

import com.example.rural_tutoring.service.FileService;
import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class MinioFileServiceImpl implements FileService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket.files}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Override
    public String uploadFile(MultipartFile file, String directory) {
        try {
            // 生成文件名
            String fileName = generateFileName(file.getOriginalFilename());
            String objectName = directory + "/" + fileName;

            // 检查存储桶是否存在，不存在则创建
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            // 上传文件
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            // 返回相对路径
            return objectName;
            
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage(), e);
        }
    }

    @Override
    public String getFileUrl(String objectName) {
        try {
            // 生成预签名URL
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(7, TimeUnit.DAYS) // URL有效期7天
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("获取文件访问URL失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteFile(String objectName) {
        try {
            // 删除文件
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("文件删除失败: " + e.getMessage(), e);
        }
    }

    private String generateFileName(String originalFilename) {
        // 生成UUID作为文件名
        String uuid = UUID.randomUUID().toString();
        // 获取文件扩展名
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        return uuid + extension;
    }
} 