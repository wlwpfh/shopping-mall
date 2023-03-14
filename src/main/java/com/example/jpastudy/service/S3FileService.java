package com.example.jpastudy.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3FileService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    // S3에 파일 업로드
    public void uploadFile(MultipartFile multipartFile, String fileName) throws IOException {
        ObjectMetadata objectMetadata=createObjectMetaData(multipartFile);
        try(InputStream inputStream = multipartFile.getInputStream()){
            amazonS3Client.putObject(
                    new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                );
        }catch (IOException e){
            throw new IOException(String.format("스트림을 가져오는 과정에서 에러가 발생하였습니다."));
        }

    }

    // ObjectMetaData 생성
    private ObjectMetadata createObjectMetaData(MultipartFile file){
        ObjectMetadata objectMetadata=new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        return objectMetadata;
    }

    // 파일명 랜덤값으로 생성
    private String createFileName(String originalFileName){
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    // 파일 확장자 가져오기 
    private String getFileExtension(String fileName){
        return fileName.substring(fileName.indexOf('.'));
    }


    //파일 삭제하기
    public void deleteFile(String file){
        DeleteObjectRequest deleteObjectRequest=new DeleteObjectRequest(bucket, file);
        amazonS3Client.deleteObject(deleteObjectRequest);
    }
}
