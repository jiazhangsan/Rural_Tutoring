package com.example.rural_tutoring.controller;

import com.example.rural_tutoring.config.Result;
import com.example.rural_tutoring.config.UserContextInterceptor;
import com.example.rural_tutoring.entity.Homework;
import com.example.rural_tutoring.entity.HomeworkAttachment;
import com.example.rural_tutoring.service.FileService;
import com.example.rural_tutoring.service.HomeworkService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "作业管理", description = "作业相关的CRUD操作")
@RestController
@RequestMapping("/api/homework")
public class HomeworkController {
    
    @Autowired
    private HomeworkService homeworkService;
    
    @Autowired
    private FileService fileService;

    @Autowired
    private ObjectMapper objectMapper;

    @Operation(summary = "发布作业", description = "给指定课程发布作业，支持多个附件")
    @PostMapping("/{lessonId}")
    public Result<Homework> publishHomework(
            @Parameter(description = "课程ID") @PathVariable Integer lessonId,
            @Parameter(description = "作业信息") @RequestPart("homework") String homeworkJson,
            @Parameter(description = "作业附件列表") @RequestPart(value = "files", required = false) MultipartFile[] files) {
        // 获取当前登录用户ID（教师ID）
        Integer currentUserId = UserContextInterceptor.getCurrentUserId();
        if (currentUserId == null) {
            return Result.error("用户ID不能为空");
        }

        try {
            // 将JSON字符串转换为Homework对象
            Homework homework = objectMapper.readValue(homeworkJson, Homework.class);
            
            List<HomeworkAttachment> attachments = new ArrayList<>();
            List<String> uploadedFiles = new ArrayList<>();

            // 如果有文件，先上传文件
            if (files != null && files.length > 0) {
                for (MultipartFile file : files) {
                    if (file != null && !file.isEmpty()) {
                        // 验证文件类型
                        String originalFilename = file.getOriginalFilename();
                        if (!isValidFileType(originalFilename)) {
                            // 删除已上传的文件
                            for (String fileUrl : uploadedFiles) {
                                fileService.deleteFile(fileUrl);
                            }
                            return Result.error("不支持的文件类型，仅支持PDF和Word文件：" + originalFilename);
                        }
                        
                        // 上传文件
                        String fileUrl = fileService.uploadFile(file, "homework");
                        uploadedFiles.add(fileUrl);

                        // 创建附件记录
                        HomeworkAttachment attachment = new HomeworkAttachment();
                        attachment.setOriginalFilename(originalFilename);
                        attachment.setFileUrl(fileUrl);
                        attachment.setFileSize(file.getSize());
                        attachment.setFileType(getFileExtension(originalFilename));
                        attachment.setUploadTime(LocalDateTime.now());
                        attachment.setUploadedBy(currentUserId);
                        attachments.add(attachment);
                    }
                }
            }

            // 设置作业信息
            homework.setLessonId(lessonId);
            homework.setCreatedBy(currentUserId);
            homework.setCreatedAt(LocalDateTime.now());
            homework.setStatus(Homework.HomeworkStatus.PUBLISHED);
            homework.setAttachments(attachments);

            // 保存作业和附件信息
            boolean success = homeworkService.addHomeworkWithAttachments(homework);
            if (success) {
            return Result.success(homework);
        } else {
                // 如果保存失败，删除已上传的文件
                for (String fileUrl : uploadedFiles) {
                    fileService.deleteFile(fileUrl);
                }
                return Result.error("发布失败");
        }
        } catch (IOException e) {
            return Result.error("数据处理失败：" + e.getMessage());
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @Operation(summary = "下载作业附件", description = "下载作业的指定附件")
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam Integer attachmentId) {
        try {
            // 获取附件信息
            HomeworkAttachment attachment = homeworkService.getHomeworkAttachment(attachmentId);
            if (attachment == null) {
                return ResponseEntity.notFound().build();
            }

            // 构建文件路径
            Path filePath = Paths.get(attachment.getFileUrl());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            // 设置响应头，支持中文文件名
            String filename = attachment.getOriginalFilename();
            String encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
            String contentDisposition = "attachment; filename*=UTF-8''" + encodedFilename;

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/test")
    public String test() {
        return "HomeworkController is working!";
    }

    private boolean isValidFileType(String filename) {
        if (filename == null) {
            return false;
        }else {
            filename = filename.toLowerCase();
        }
        return filename.endsWith(".pdf") || 
               filename.endsWith(".doc") || 
               filename.endsWith(".docx");
    }

    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(lastDotIndex + 1).toLowerCase() : "";
    }
}
