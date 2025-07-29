package com.example.rural_tutoring.controller;

import com.example.rural_tutoring.config.Result;
import com.example.rural_tutoring.service.FrontendImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 前端图片上传控制器
 */
@Tag(name = "前端图片管理", description = "前端图片上传和管理")
@RestController
@RequestMapping("/api/frontend/images")
public class FrontendImageController {

    @Autowired
    private FrontendImageService frontendImageService;

    @Operation(summary = "上传单个前端图片", description = "上传单个前端使用的图片到MinIO的education桶的指定文件夹")
    @PostMapping("/upload")
    public Result<String> uploadImage(
            @Parameter(description = "图片文件") @RequestParam("file") MultipartFile file,
            @Parameter(description = "文件夹名称（可选，默认为frontend）") @RequestParam(value = "frontend", required = false) String folder) {
        try {
            // 验证文件类型
            if (!isValidImageType(file.getOriginalFilename())) {
                return Result.error("不支持的文件类型，仅支持图片格式（jpg, jpeg, png, gif, webp）");
            }

            // 验证文件大小（限制为50MB）
            if (file.getSize() > 50 * 1024 * 1024) {
                return Result.error("文件大小不能超过50MB");
            }

            // 验证文件是否为空
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }

            String imageUrl = frontendImageService.uploadFrontendImage(file, folder);
            return Result.success(imageUrl);
        } catch (Exception e) {
            return Result.error("上传失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量上传多个前端图片", description = "同时上传多个前端使用的图片到MinIO的education桶的指定文件夹")
    @PostMapping("/upload-multiple")
    public Result<List<String>> uploadMultipleImages(
            @Parameter(description = "图片文件列表") @RequestParam("files") List<MultipartFile> files,
            @Parameter(description = "文件夹名称（可选，默认为frontend）") @RequestParam(value = "frontend", required = false) String folder) {
        try {
            // 验证文件列表
            if (files == null || files.isEmpty()) {
                return Result.error("文件列表不能为空");
            }

            // 验证文件数量（限制最多10个文件）
            if (files.size() > 10) {
                return Result.error("一次最多只能上传10个文件");
            }

            // 验证每个文件
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    return Result.error("文件不能为空");
                }

                if (!isValidImageType(file.getOriginalFilename())) {
                    return Result.error("不支持的文件类型，仅支持图片格式（jpg, jpeg, png, gif, webp）");
                }

                if (file.getSize() > 50 * 1024 * 1024) {
                    return Result.error("文件大小不能超过50MB");
                }
            }

            List<String> imageUrls = frontendImageService.uploadMultipleFrontendImages(files, folder);
            return Result.success(imageUrls);
        } catch (Exception e) {
            return Result.error("批量上传失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除单个前端图片", description = "删除指定的前端图片")
    @DeleteMapping("/delete")
    public Result<Boolean> deleteImage(
            @Parameter(description = "图片URL") @RequestParam("imageUrl") String imageUrl) {
        try {
            if (imageUrl == null || imageUrl.trim().isEmpty()) {
                return Result.error("图片URL不能为空");
            }

            boolean success = frontendImageService.deleteFrontendImage(imageUrl);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量删除多个前端图片", description = "同时删除多个指定的前端图片")
    @DeleteMapping("/delete-multiple")
    public Result<Integer> deleteMultipleImages(
            @Parameter(description = "图片URL列表") @RequestBody List<String> imageUrls) {
        try {
            if (imageUrls == null || imageUrls.isEmpty()) {
                return Result.error("图片URL列表不能为空");
            }

            // 验证URL数量（限制最多20个）
            if (imageUrls.size() > 20) {
                return Result.error("一次最多只能删除20个图片");
            }

            int successCount = frontendImageService.deleteMultipleFrontendImages(imageUrls);
            return Result.success(successCount);
        } catch (Exception e) {
            return Result.error("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 验证图片文件类型
     */
    private boolean isValidImageType(String filename) {
        if (filename == null) {
            return false;
        }
        String extension = filename.toLowerCase();
        return extension.endsWith(".jpg") || 
               extension.endsWith(".jpeg") || 
               extension.endsWith(".png") || 
               extension.endsWith(".gif") || 
               extension.endsWith(".webp");
    }
} 