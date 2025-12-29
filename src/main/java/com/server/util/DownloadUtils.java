package com.server.util;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class DownloadUtils {

    public static ResponseEntity<InputStreamResource> buildDownloadResponse(
            File file,
            String downloadName
    ) {

        try {
            String encodedFilename = URLEncoder.encode(downloadName, StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            /*HttpHeaders headers = new HttpHeaders();
            headers.add(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename*=UTF-8''" + encodedFilename
            );

            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(file.length());*/

            InputStreamResource resource =
                    new InputStreamResource(new FileInputStream(file));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename*=UTF-8''" + encodedFilename)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found");
        }
    }
}