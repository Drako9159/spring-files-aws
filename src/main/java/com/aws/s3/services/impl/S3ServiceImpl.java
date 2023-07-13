package com.aws.s3.services.impl;

import com.aws.s3.services.IS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.Response;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3ServiceImpl implements IS3Service {

    @Value("${upload.s3.localPath}")
    private String localPath;

    private final S3Client s3Client;

    @Autowired
    public S3ServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        try {
            String fileName = file.getOriginalFilename();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket("bucket-youtube-dif")
                    .key(fileName)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
            return "File upload successfully!";
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }


    public String downloadFile(String fileName) throws IOException {
        if (!doesObjectExists(fileName)) {
            return "File does not exist!";
        }
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket("bucket-youtube-dif")
                .key(fileName)
                .build();
        ResponseInputStream<GetObjectResponse> result = s3Client.getObject(request);
        String localPath2 = "/Users/" + System.getProperty("user.name") + "/Downloads";
        try (FileOutputStream fos = new FileOutputStream(localPath + fileName)) {
            byte[] read_buff = new byte[1024];
            int read_len = 0;
            while ((read_len = result.read(read_buff)) > 0) {
                fos.write(read_buff, 0, read_len);
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
        return "File downloaded!!";
    }

    public List<String> listFiles() throws IOException {
        try{
            ListObjectsRequest listObjectsRequest = ListObjectsRequest.builder()
                    .bucket("bucket-youtube-dif")
                    .build();
            List<S3Object> objects = s3Client.listObjects(listObjectsRequest).contents();
            List<String> fileNames = new ArrayList<>();
            for(S3Object object: objects){
                fileNames.add(object.key());
            }
            return fileNames;
        } catch (S3Exception e){
            throw new IOException(e.getMessage());
        }
    }

    private boolean doesObjectExists(String objectKey) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket("bucket-youtube-dif")
                    .key(objectKey)
                    .build();
            s3Client.headObject(headObjectRequest);
            return true;
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                return false;
            }
        }
        return true;
    }

}
