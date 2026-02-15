package com.example.solacademy.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {

 private final S3Client s3Client;

 @Value("${aws.s3.bucket}")
 private String bucketName;

 public S3Service(@Value("${aws.accessKeyId}") String accessKeyId,
                  @Value("${aws.secretKey}") String secretKey,
                  @Value("${aws.region}") String region) {
     AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretKey);
     this.s3Client = S3Client.builder()
             .region(Region.of(region))
             .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
             .build();
 }

 public void uploadFile(MultipartFile file, String selectedPath) throws IOException {
     String key = selectedPath + "/" + Paths.get(file.getOriginalFilename()).getFileName().toString();
     PutObjectRequest putObjectRequest = PutObjectRequest.builder()
             .bucket(bucketName)
             .key(key)
             .contentType(file.getContentType()) // Añadir el Content-Type
             .build();
     RequestBody requestBody = RequestBody.fromInputStream(file.getInputStream(), file.getSize());
     s3Client.putObject(putObjectRequest, requestBody);
 }
 
 public List<String> listFiles(String studentId) {
     ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
             .bucket(bucketName)
             .prefix(studentId + "/")
             .build();
     ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);

     return listObjectsV2Response.contents().stream()
             .map(S3Object::key)
             .collect(Collectors.toList());
 }

 public List<String> listFiles() {
     ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
             .bucket(bucketName)
             .build();
     ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);

     return listObjectsV2Response.contents().stream()
             .map(S3Object::key)
             .collect(Collectors.toList());
 }
 
 public void deleteFile(String key) {
     DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
             .bucket(bucketName)
             .key(key)
             .build();
     s3Client.deleteObject(deleteObjectRequest);
 }
 
 public void createFolder(String folderPath, String selectedPath) {
	 String selectedKey = "";
	 if(folderPath.isEmpty()) {
		 selectedKey = selectedPath + "/";
	 } else {
		 selectedKey = selectedPath + "/" + folderPath + "/"; 
	 }
     PutObjectRequest putObjectRequest = PutObjectRequest.builder()
             .bucket(bucketName)
             .key(selectedKey)
             .build();
     s3Client.putObject(putObjectRequest, RequestBody.empty());
 }
 
 public void deleteFolder(String folderPath) {
     ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
             .bucket(bucketName)
             .prefix(folderPath + "/")
             .build();
     ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);

     List<String> keysToDelete = listObjectsV2Response.contents().stream()
             .map(s3Object -> s3Object.key())
             .collect(Collectors.toList());

     for (String key : keysToDelete) {
         DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                 .bucket(bucketName)
                 .key(key)
                 .build();
         s3Client.deleteObject(deleteObjectRequest);
     }

     // Delete the folder itself
     DeleteObjectRequest deleteFolderRequest = DeleteObjectRequest.builder()
             .bucket(bucketName)
             .key(folderPath + "/")
             .build();
     s3Client.deleteObject(deleteFolderRequest);
 }

}
