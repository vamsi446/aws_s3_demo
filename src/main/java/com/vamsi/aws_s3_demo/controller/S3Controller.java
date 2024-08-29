package com.vamsi.aws_s3_demo.controller;


import com.vamsi.aws_s3_demo.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
public class S3Controller {
    private final S3Service s3Service;

    @Autowired
    public S3Controller(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/list-objects")
    public List<Map<String, Object>> listObjects(@RequestParam String bucketName) {
        List<S3Object> s3Objects = s3Service.listObjects(bucketName);
        return s3Objects.stream()
                .map(s3Object -> {
                    Map<String, Object> objectData = new HashMap<>();
                    objectData.put("key", s3Object.key());
                    objectData.put("size", s3Object.size());
                    return objectData;
                })
                .collect(Collectors.toList());
    }



    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadObject(@RequestParam String bucketName,
                                              @RequestParam String key) {
        try {
            InputStream inputStream = s3Service.downloadObject(bucketName,key);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + key + "\"")
                    .body(new InputStreamResource(inputStream));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/upload-file")
    public String uploadFile(@RequestParam String bucketName,
                             @RequestParam String keyName,
                             @RequestParam String filePath) {
        s3Service.uploadFile(bucketName, keyName, filePath);
        return "File uploaded successfully!";
    }
}
