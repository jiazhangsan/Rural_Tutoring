package com.example.rural_tutoring.controller;

import com.example.rural_tutoring.config.Result;
import com.example.rural_tutoring.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 视频上传控制器
 */
@Tag(name = "视频管理", description = "视频上传和管理")
@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Operation(summary = "上传单个视频", description = "上传单个视频到MinIO的education桶的指定文件夹")
    @PostMapping("/upload")
    public Result<String> uploadVideo(
            @Parameter(description = "视频文件") @RequestParam("file") MultipartFile file,
            @Parameter(description = "文件夹名称（可选，默认为video）") @RequestParam(value = "folder", required = false) String folder) {
        try {
            // 验证文件类型
            if (!isValidVideoType(file.getOriginalFilename())) {
                return Result.error("不支持的文件类型，仅支持视频格式（mp4, avi, mov, wmv, flv, webm, mkv）");
            }

            // 验证文件大小（限制为500MB）
            if (file.getSize() > 500 * 1024 * 1024) {
                return Result.error("文件大小不能超过500MB");
            }

            // 验证文件是否为空
            if (file.isEmpty()) {
                return Result.error("文件不能为空");
            }

            String videoUrl = videoService.uploadVideo(file, folder);
            return Result.success(videoUrl);
        } catch (Exception e) {
            return Result.error("上传失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量上传多个视频", description = "同时上传多个视频到MinIO的education桶的指定文件夹")
    @PostMapping("/upload-multiple")
    public Result<List<String>> uploadMultipleVideos(
            @Parameter(description = "视频文件列表") @RequestParam("files") List<MultipartFile> files,
            @Parameter(description = "文件夹名称（可选，默认为video）") @RequestParam(value = "folder", required = false) String folder) {
        try {
            // 验证文件列表
            if (files == null || files.isEmpty()) {
                return Result.error("文件列表不能为空");
            }

            // 验证文件数量（限制最多5个文件）
            if (files.size() > 5) {
                return Result.error("一次最多只能上传5个视频文件");
            }

            // 验证每个文件
            for (MultipartFile file : files) {
                if (file.isEmpty()) {
                    return Result.error("文件不能为空");
                }

                if (!isValidVideoType(file.getOriginalFilename())) {
                    return Result.error("不支持的文件类型，仅支持视频格式（mp4, avi, mov, wmv, flv, webm, mkv）");
                }

                if (file.getSize() > 500 * 1024 * 1024) {
                    return Result.error("文件大小不能超过500MB");
                }
            }

            List<String> videoUrls = videoService.uploadMultipleVideos(files, folder);
            return Result.success(videoUrls);
        } catch (Exception e) {
            return Result.error("批量上传失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除单个视频", description = "删除指定的视频")
    @DeleteMapping("/delete")
    public Result<Boolean> deleteVideo(
            @Parameter(description = "视频URL") @RequestParam("videoUrl") String videoUrl) {
        try {
            if (videoUrl == null || videoUrl.trim().isEmpty()) {
                return Result.error("视频URL不能为空");
            }

            boolean success = videoService.deleteVideo(videoUrl);
            if (success) {
                return Result.success(true);
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量删除多个视频", description = "同时删除多个指定的视频")
    @DeleteMapping("/delete-multiple")
    public Result<Integer> deleteMultipleVideos(
            @Parameter(description = "视频URL列表") @RequestBody List<String> videoUrls) {
        try {
            if (videoUrls == null || videoUrls.isEmpty()) {
                return Result.error("视频URL列表不能为空");
            }

            // 验证URL数量（限制最多10个）
            if (videoUrls.size() > 10) {
                return Result.error("一次最多只能删除10个视频");
            }

            int successCount = videoService.deleteMultipleVideos(videoUrls);
            return Result.success(successCount);
        } catch (Exception e) {
            return Result.error("批量删除失败: " + e.getMessage());
        }
    }

    /**
     * 验证视频文件类型
     */
    private boolean isValidVideoType(String filename) {
        if (filename == null) {
            return false;
        }
        String extension = filename.toLowerCase();
        return extension.endsWith(".mp4") || 
               extension.endsWith(".avi") || 
               extension.endsWith(".mov") || 
               extension.endsWith(".wmv") || 
               extension.endsWith(".flv") || 
               extension.endsWith(".webm") || 
               extension.endsWith(".mkv");
    }
} 