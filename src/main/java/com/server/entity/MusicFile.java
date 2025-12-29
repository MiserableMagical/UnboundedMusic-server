package com.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "music_files")
public class MusicFile {

    @Id
    private String id;

    /** 文件原始名 */
    private String filename;

    /** 实际存储路径 */
    private String storagePath;

    /** MIME 类型 */
    private String contentType;

    /** 是否为公共资源 */
    private boolean isPublic;

    /** 所属用户（公共库为 null） */
    private String ownerId;

    /** 上传时间 */
    private LocalDateTime createdAt;

    public String getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setUploadTime(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public String getContentType() {
        return contentType;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}