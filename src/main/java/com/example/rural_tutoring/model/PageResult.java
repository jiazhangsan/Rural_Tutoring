package com.example.rural_tutoring.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * 分页结果包装类
 * 
 * @author jiashuhang
 * @date 2025-01-07
 */
@Schema(description = "分页结果")
public class PageResult<T> {
    
    @Schema(description = "数据列表")
    private List<T> records;      // 数据列表
    
    @Schema(description = "总记录数", example = "100")
    private long total;           // 总记录数
    
    @Schema(description = "当前页码", example = "1")
    private long current;         // 当前页码
    
    @Schema(description = "每页大小", example = "10")
    private long size;            // 每页大小
    
    @Schema(description = "总页数", example = "10")
    private long pages;           // 总页数
    
    public PageResult() {}
    
    public PageResult(List<T> records, long total, long current, long size) {
        this.records = records;
        this.total = total;
        this.current = current;
        this.size = size;
        this.pages = (total + size - 1) / size; // 计算总页数
    }
    
    // Getters and Setters
    public List<T> getRecords() { return records; }
    public void setRecords(List<T> records) { this.records = records; }
    
    public long getTotal() { return total; }
    public void setTotal(long total) { this.total = total; }
    
    public long getCurrent() { return current; }
    public void setCurrent(long current) { this.current = current; }
    
    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }
    
    public long getPages() { return pages; }
    public void setPages(long pages) { this.pages = pages; }
} 