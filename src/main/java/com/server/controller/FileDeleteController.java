package com.server.controller;

import com.server.entity.UserPrincipal;
import com.server.service.CloudMusicService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FileDeleteController {
    private final CloudMusicService cloudMusicService;

    public FileDeleteController(CloudMusicService cloudMusicService) {
        this.cloudMusicService = cloudMusicService;
    }

    @DeleteMapping("/music/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable String id,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        cloudMusicService.deleteMusic(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
