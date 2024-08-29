package com.vamsi.aws_s3_demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.File;
import java.io.InputStream;
import java.util.List;


@Service
public class S3Service {

    @Autowired
    private  S3Client s3Client;

    public List<S3Object> listObjects(String bucketName) {
        ListObjectsRequest listObjectsRequest = ListObjectsRequest
                .builder()
                .bucket(bucketName)
                .build();

        ListObjectsResponse response = s3Client.listObjects(listObjectsRequest);
        return response.contents();
    }

    public void uploadFile(String bucketName, String keyName, String filePath) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        s3Client.putObject(putObjectRequest, new File(filePath).toPath());
    }

    public InputStream downloadObject(String bucketName, String key) {

        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            return s3Client.getObject(getObjectRequest);
        } catch (S3Exception e) {
            // Handle the exception
            throw new RuntimeException("Error occurred while fetching object from S3", e);
        }
    }

}
