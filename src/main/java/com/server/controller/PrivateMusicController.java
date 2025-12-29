package com.server.controller;

import com.server.entity.MusicFile;
import com.server.service.CloudMusicService;
import com.server.entity.UserPrincipal;
import jakarta.annotation.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.server.util.DownloadUtils.buildDownloadResponse;

@RestController
@RequestMapping("/music/private")
public class PrivateMusicController {
    private final CloudMusicService cloudMusicService;

    public PrivateMusicController(CloudMusicService cloudMusicService) {
        this.cloudMusicService = cloudMusicService;
    }

    @PostMapping("/upload")
    public void upload(
            @RequestParam MultipartFile file,
            @AuthenticationPrincipal UserPrincipal user) {

        cloudMusicService.upload(file, user.getId(), false);
    }

    @GetMapping("/list")
    public List<MusicFile> list(@AuthenticationPrincipal UserPrincipal user) {
        return cloudMusicService.listPrivateMusic(user.getId());
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> download(
            @PathVariable String id,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        return cloudMusicService.download(id, user.getId());
    }
}
