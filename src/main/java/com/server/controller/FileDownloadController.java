package com.server.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import com.server.entity.FileRecord;
import com.server.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@RestController
public class FileDownloadController {

    private final FileService fileService;

    public FileDownloadController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable String id) {

        return fileService.getRecord(id)
                .map(record -> {
                    File file = new File(record.getStoredPath());
                    Resource resource = new FileSystemResource(file);

                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION,
                                    "attachment; filename=\"" + record.getOriginalName() + "\"")
                            .body(resource);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}