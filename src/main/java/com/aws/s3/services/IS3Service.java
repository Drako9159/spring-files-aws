package com.aws.s3.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface IS3Service {
    String uploadFile(MultipartFile file) throws IOException;
}
