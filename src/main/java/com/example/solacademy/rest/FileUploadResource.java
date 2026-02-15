package com.example.solacademy.rest;

import com.example.solacademy.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = {"http://solacademy.qdomingo.com", "http://localhost:4200", "http://localhost:4300",
"http://solacademy-aws.qdomingo.com:4200"})
@RequestMapping("/api/files")
public class FileUploadResource  {

 @Autowired
 private S3Service s3Service;
 
 @PostMapping("/upload")
 public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("selectedPath") String selectedPath) {
     try {
         s3Service.uploadFile(file, selectedPath);
         return ResponseEntity.ok("{\"message\": \"File uploaded successfully\"}");
     } catch (IOException e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                 .body("{\"message\": \"Error uploading file\"}");
     }
 }

 @GetMapping("/getFiles")
 public ResponseEntity<List<FileResponse>> listFiles() {
     List<String> fileKeys = s3Service.listFiles();
     List<FileResponse> files = fileKeys.stream()
             .map(key -> new FileResponse(key, generatePresignedUrl(key)))
             .collect(Collectors.toList());
     return new ResponseEntity<>(files, HttpStatus.OK);
 }
 
 @GetMapping("/getFiles/{id}")
 public ResponseEntity<List<FileResponse>> listFiles(@PathVariable String id) {
     List<String> fileKeys = s3Service.listFiles(id);
     List<FileResponse> files = fileKeys.stream()
             .map(key -> new FileResponse(key, generatePresignedUrl(key)))
             .collect(Collectors.toList());
     return ResponseEntity.ok(files);
 }
 
 @DeleteMapping("/delete")
 public ResponseEntity<String> deleteFile(@RequestParam("key") String key) {
     s3Service.deleteFile(key);
     return ResponseEntity.ok("{\"message\": \"File deleted successfully\"}");
 }
 
 @PostMapping("/createFolder")
 public ResponseEntity<String> createFolder(@RequestParam("folderPath") String folderPath, @RequestParam("selectedPath") String selectedPath) {
     s3Service.createFolder(folderPath, selectedPath);
     return ResponseEntity.ok("{\"message\": \"Folder created successfully\"}");
 }
 
 @DeleteMapping("/deleteFolder")
 public ResponseEntity<String> deleteFolder(@RequestParam("folderPath") String folderPath) {
     s3Service.deleteFolder(folderPath);
     return ResponseEntity.ok("{\"message\": \"Folder and its contents deleted successfully\"}");
 }

 private String generatePresignedUrl(String key) {
     // Implementa la lógica para generar una URL prefirmada para el archivo
     // Puedes usar el método presign de S3Client para generar la URL
     return "https://solacademy.s3.us-east-1.amazonaws.com/" + key;
 }

 public static class FileResponse {
     private String name;
     private String url;

     public FileResponse(String name, String url) {
         this.name = name;
         this.url = url;
     }

     public String getName() {
         return name;
     }

     public void setName(String name) {
         this.name = name;
     }

     public String getUrl() {
         return url;
     }

     public void setUrl(String url) {
         this.url = url;
     }
 }
}
