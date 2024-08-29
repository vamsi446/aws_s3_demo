package com.vamsi.aws_s3_demo.model;

import software.amazon.awssdk.services.s3.model.S3Object;

public class S3ObjectDTO {
    private String key;
    private long size;

    public S3ObjectDTO(S3Object s3Object) {
        this.key = s3Object.key();
        this.size = s3Object.size();
    }
}
