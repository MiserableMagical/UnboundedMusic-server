package com.server.controller;

import com.server.entity.MusicFile;
import com.server.entity.UserPrincipal;
import com.server.service.CloudMusicService;
import jakarta.annotation.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.server.util.DownloadUtils.buildRangeResponse;

@RestController
@RequestMapping("/music/public")
public class PublicMusicController {
    private final CloudMusicService cloudMusicService;

    public PublicMusicController(CloudMusicService cloudMusicService) {
        this.cloudMusicService = cloudMusicService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(
            @RequestParam MultipartFile file,
            @AuthenticationPrincipal UserPrincipal user) {

        String uuid = cloudMusicService.upload(file, user.getId(), true);
        return ResponseEntity.ok(uuid);
    }

    @GetMapping("/list")
    public List<MusicFile> listPublic() {
        return cloudMusicService.listPublicMusic();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ResourceRegion> download(
            @PathVariable String id,
            @RequestHeader HttpHeaders headers
    ) throws IOException {

        File file = cloudMusicService.getFile(id, null);
        UrlResource resource = new UrlResource(file.toURI());

        long contentLength = resource.contentLength();

        ResourceRegion region;

        if (headers.getRange().isEmpty()) {
            region = new ResourceRegion(resource, 0, contentLength);
        } else {
            HttpRange range = headers.getRange().get(0);
            long start = range.getRangeStart(contentLength);
            long end = range.getRangeEnd(contentLength);
            region = new ResourceRegion(resource, start, end - start + 1);
        }

        return ResponseEntity
                .status(HttpStatus.PARTIAL_CONTENT)
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(region);
    }
}