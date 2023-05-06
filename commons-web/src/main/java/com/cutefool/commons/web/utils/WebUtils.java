package com.cutefool.commons.web.utils;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.Instant;
import java.util.Objects;

public final class WebUtils {

    public static ResponseEntity<Resource> download(File file) {
        try {
            // 浏览器下载文件名乱码问题
            HttpHeaders headers = new HttpHeaders();
            String fileName = URLEncoder.encode(Objects.requireNonNull(file.getName()), "utf-8");
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", "attachment;filename=" + fileName);
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            headers.add("Access-Control-Expose-Headers", "Content-Disposition");
            headers.add("Last-Modified", String.valueOf(Instant.now().toEpochMilli()));
            headers.add("ETag", String.valueOf(Instant.now().toEpochMilli()));
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new FileSystemResource(file));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

}
