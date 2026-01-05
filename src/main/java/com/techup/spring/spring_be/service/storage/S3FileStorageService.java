package com.techup.spring.spring_be.service.storage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
@Service
@RequiredArgsConstructor
public class S3FileStorageService {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    public String uploadPostImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;

        System.out.println("uploadPostImage 서비스");

        // ✅ 확장자만 유지 (없으면 빈 문자열)
        String original = file.getOriginalFilename();
        String ext = "";
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf("."));
        }

        // ✅ key는 안전하게 UUID 기반으로만
        String key = "posts/" + LocalDate.now() + "/" + UUID.randomUUID() + ext;
        System.out.println("key: " + key);

        try {
            PutObjectRequest putReq = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    putReq,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

        }catch (S3Exception e){
            System.out.println("S3 업로드 실패 "+e.getMessage());
            throw new RuntimeException("S3 업로드 중 오류가 발생했습니다.");
        }



        return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;
    }
}
