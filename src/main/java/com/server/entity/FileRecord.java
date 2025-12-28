package com.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class FileRecord {

    @Id
    private String id;          // 文件 ID

    private String originalName;
    private String storedPath;
    private LocalDateTime uploadTime;

    public FileRecord() {}

    public FileRecord(String id, String originalName, String storedPath) {
        this.id = id;
        this.originalName = originalName;
        this.storedPath = storedPath;
        this.uploadTime = LocalDateTime.now();
    }

    // getter / setter（可用 IDEA 自动生成）
    public String getId() { return id; }
    public String getOriginalName() { return originalName; }
    public String getStoredPath() { return storedPath; }
    public LocalDateTime getUploadTime() { return uploadTime; }
}