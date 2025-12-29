package com.server.controller;

import com.server.entity.MusicFile;
import com.server.service.CloudMusicService;
import jakarta.annotation.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.server.util.DownloadUtils.buildDownloadResponse;

@RestController
@RequestMapping("/music/public")
public class PublicMusicController {
    private final CloudMusicService cloudMusicService;

    public PublicMusicController(CloudMusicService cloudMusicService) {
        this.cloudMusicService = cloudMusicService;
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