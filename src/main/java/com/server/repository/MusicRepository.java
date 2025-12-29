package com.server.repository;

import com.server.entity.MusicFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicRepository extends JpaRepository<MusicFile, String> {

    // 查询公共音乐
    List<MusicFile> findByIsPublicTrue();

    // 查询某个用户的私有音乐
    List<MusicFile> findByOwnerId(String ownerId);
}