package com.techup.spring.spring_be.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsS3Config {

    // application.yml 의 cloud.aws.region.static 읽기
    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                // ✅ 환경변수/프로파일/EC2 IAM Role 등 “기본 체인”에서 자동 탐색
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }
}