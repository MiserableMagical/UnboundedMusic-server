package com.server.service;

import com.server.entity.MusicFile;
import com.server.exception.BusinessException;
import com.server.repository.MusicRepository;
import com.server.util.DownloadUtils;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CloudMusicService {

    private final MusicRepository musicRepository;

    // 文件根目录，例如：server-data/music
    private final Path rootDir = Paths.get("server-data/music");

    public CloudMusicService(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    /* ================= 公共音乐 ================= */

    public List<MusicFile> listPublicMusic() {
        return musicRepository.findByIsPublicTrue();
    }

    /* ================= 私有云盘 ================= */

    public List<MusicFile> listPrivateMusic(String userId) {
        return musicRepository.findByOwnerId(userId);
    }

    /* ================= 上传 ================= */

    public String upload(
            MultipartFile file,
            String userId,
            boolean isPublic
    ) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        try {
            Files.createDirectories(rootDir);

            String id = UUID.randomUUID().toString();
            String filename = id + "_" + file.getOriginalFilename();

            Path target = rootDir.resolve(filename);

            Files.copy(file.getInputStream(), target);

            MusicFile music = new MusicFile();
            music.setId(id);
            music.setFilename(file.getOriginalFilename());
            music.setStoragePath(target.toString());
            music.setPublic(isPublic);
            music.setOwnerId(userId);
            music.setUploadTime(LocalDateTime.now());

            musicRepository.save(music);

            return id;

        } catch (IOException e) {
            throw new RuntimeException("Upload failed");
        }
    }

    /* ================= 下载 ================= */

    public File getFile(String musicId, String currentUserId)
    {
        MusicFile music = musicRepository.findById(musicId)
                .orElseThrow(() -> new BusinessException(404, "Music Not Found"));

        // 🔐 权限判断
        if (!music.isPublic()
                && !music.getOwnerId().equals(currentUserId)) {
            throw new BusinessException(401, "No Access to this file");
        }

        File file = new File(music.getStoragePath());
        if (!file.exists()) {
            throw new RuntimeException("File missing on server");
        }
        return file;
    }

    /*public ResponseEntity<InputStreamResource> download(
            String musicId,
            String currentUserId
    ) {
        return DownloadUtils.buildDownloadResponse(
                file,
                music.getFilename()
        );
    }*/
    @Transactional
    public void deleteMusic(String musicId, String userId) {

        MusicFile music = musicRepository.findById(musicId)
                .orElseThrow(() -> new RuntimeException("Music not found"));

        // 1️⃣ 权限校验
        if (!music.isPublic()) {
            if (!music.getOwnerId().equals(userId)) {
                throw new RuntimeException("No permission to delete this file");
            }
        } else {
            // 公共音乐（如果你暂时没管理员系统）
            throw new RuntimeException("Public music cannot be deleted");
        }

        // 2️⃣ 删除物理文件
        Path path = Paths.get(music.getStoragePath());
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }

        // 3️⃣ 删除数据库记录
        musicRepository.delete(music);
    }
}