package com.example.campusfinder.core.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static java.util.UUID.randomUUID;

/**
 * packageName    : com.example.campusfinder.core.util
 * fileName       : S3Domain
 * author         : tlswl
 * date           : 2024-10-01
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-10-01        tlswl       최초 생성
 */
@Component
public class S3Domain {
    private AmazonS3 s3client;
    private String bucketName;

    public S3Domain(@Value("${cloud.aws.s3.bucket}") String bucketName, AmazonS3 s3client){
        this.bucketName=bucketName;
        this.s3client=s3client;
    }

    public String uploadMultipartFile(MultipartFile multipartFile) throws IOException {
        String fileName = randomUUID().toString() + ".jpg";
        InputStream inputStream = multipartFile.getInputStream();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        uploadFile(fileName, inputStream, metadata);
        return getFileUrl(fileName);
    }

    private void uploadFile(String fileName, InputStream inputStream, ObjectMetadata metadata) {
        s3client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));
    }

    public void deleteFile(String imageUrl) {
        // S3 버킷에서 이미지 삭제
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        s3client.deleteObject(bucketName, fileName);
    }

    private String getFileUrl(String fileName) {
        return s3client.getUrl(bucketName, fileName).toString();
    }
}
