package com.example.rural_tutoring.model;

import java.util.List;

/**
 * 分页结果包装类
 */
public class PageResult<T> {
    private List<T> records;      // 数据列表
    private long total;           // 总记录数
    private long current;         // 当前页码
    private long size;            // 每页大小
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