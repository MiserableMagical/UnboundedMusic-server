package com.server.controller;

import com.server.entity.FileRecord;
import com.server.service.FileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@RestController
public class FileUploadController {

    private static final String UPLOAD_DIR = "C:/server-data/uploads/";

    private final FileService fileService;

    public FileUploadController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("upload called");

        String id = String.valueOf(System.currentTimeMillis());
        File dir = new File(UPLOAD_DIR);
        dir.mkdirs();

        File target = new File(dir, id + "_" + file.getOriginalFilename());
        file.transferTo(target);

        // 保存数据库记录
        FileRecord record = new FileRecord(
                id,
                file.getOriginalFilename(),
                target.getAbsolutePath()
        );
        fileService.saveRecord(record);

        return "Upload success, fileId=" + id;
    }
}