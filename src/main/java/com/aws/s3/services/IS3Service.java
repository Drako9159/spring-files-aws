package com.aws.s3.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface IS3Service {
    String uploadFile(MultipartFile file) throws IOException;
    String downloadFile(String fileName) throws IOException;
    List<String> listFiles() throws IOException;
}
