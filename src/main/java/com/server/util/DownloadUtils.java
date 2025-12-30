package com.server.util;

import jakarta.annotation.Resource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class DownloadUtils {

    public static ResponseEntity<InputStreamResource> buildRangeResponse(
            File file,
            String rangeHeader
    ) throws IOException {

        long fileLength = file.length();

        // 没有 Range → 返回完整文件
        if (rangeHeader == null) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                    .contentLength(fileLength)
                    .body(new InputStreamResource(new FileInputStream(file)));
        }

        // Range: bytes=start-end
        String[] ranges = rangeHeader.replace("bytes=", "").split("-");
        long start = Long.parseLong(ranges[0]);
        long end = ranges.length > 1 && !ranges[1].isEmpty()
                ? Long.parseLong(ranges[1])
                : fileLength - 1;

        if (end >= fileLength) {
            end = fileLength - 1;
        }

        final long[] contentLength = {end - start + 1};

        RandomAccessFile raf = new RandomAccessFile(file, "r");
        raf.seek(start);

        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                if (contentLength[0] <= 0) return -1;
                contentLength[0]--;
                return raf.read();
            }

            @Override
            public void close() throws IOException {
                raf.close();
            }
        };

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_RANGE,
                "bytes " + start + "-" + end + "/" + fileLength);
        headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");
        headers.setContentLength(end - start + 1);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .headers(headers)
                .body(new InputStreamResource(inputStream));
    }
}