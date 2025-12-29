package com.server.controller;

import com.server.entity.MusicFile;
import com.server.entity.UserPrincipal;
import com.server.service.CloudMusicService;
import jakarta.annotation.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.server.util.DownloadUtils.buildDownloadResponse;

@RestController
@RequestMapping("/music/public")
public class PublicMusicController {
    private final CloudMusicService cloudMusicService;

    public PublicMusicController(CloudMusicService cloudMusicService) {
        this.cloudMusicService = cloudMusicService;
    }

    @PostMapping("/upload")
    public void upload(
            @RequestParam MultipartFile file,
            @AuthenticationPrincipal UserPrincipal user) {

        cloudMusicService.upload(file, user.getId(), true);
    }

    @GetMapping("/list")
    public List<MusicFile> listPublic() {
        return cloudMusicService.listPublicMusic();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> download(@PathVariable String id) {
        return cloudMusicService.download(id, null);
    }
}