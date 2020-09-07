package com.yosep.msa.yosfrontserver.fileIO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.yosep.msa.yosfrontserver.entity.ProductDtoForRestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
@RefreshScope
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    private String bucket = "";
    private static String s3FileURL = "";
    
    public String upload(ProductDtoForRestTemplate productDtoForRestTemplate,MultipartFile multipartFile, String dirName) throws IOException {
    	File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
    	productDtoForRestTemplate.getProductProfileImageURLs().add(s3FileURL + dirName + "/" + multipartFile.getOriginalFilename());
        return upload(uploadFile, dirName);
    }
    
    // TODO: product 수정에 따른 코드 수정 계획
//    public String upload(Product product,MultipartFile multipartFile, String dirName) throws IOException {
//    	File uploadFile = convert(multipartFile)
//                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
//    	product.getProductProfileImageURLs().add(s3FileURL + dirName + "/" + multipartFile.getOriginalFilename());
//        return upload(uploadFile, dirName);
//    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + File.separator + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }
//    private File convert(MultipartFile file) throws IOException {
//        File convertFile = new File(file.getOriginalFilename());
//        if(convertFile.createNewFile()) {
//            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
//                fos.write(file.getBytes());
//            }
////            return Optional.of(convertFile);
//            return convertFile;
//        }
//
////        return Optional.empty();
//        return null;
//    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        System.out.println(file.getOriginalFilename());
    	File convertFile = new File(file.getOriginalFilename());
//        if(convertFile.createNewFile()) {
    		convertFile.createNewFile();
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
//        }
//        System.out.println("Error!");

//        return Optional.empty();
    }
}
