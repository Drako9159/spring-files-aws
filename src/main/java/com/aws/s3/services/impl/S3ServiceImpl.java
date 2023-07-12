package com.aws.s3.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

@Service
public class S3ServiceImpl {

    private final S3Client s3Client;

    @Autowired
    public S3ServiceImpl(S3Client s3Client){

    }
}
