package com.techup.spring.spring_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Qualifier;
import java.time.Duration;

/**
 * 이메일 관련 서비스를 제공하는 클래스입니다.
 * 이메일 발송, 인증 코드 검증 등의 기능을 담당합니다.
 */
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender; // 이메일 발송을 위한 의존성
    @Qualifier("redisTemplateForMail")
    private final RedisTemplate<String, String> redisTemplate; // Redis와 상호작용하기 위한 의존성

    private static final String AUTH_CODE_PREFIX = "AUTH_CODE:"; // 인증 코드를 저장할 때 사용할 접두사
    private static final String VERIFIED_PREFIX = "VERIFIED_EMAIL:"; // 인증된 이메일을 저장할 때 사용할 접두사

    /**
     * 인증 코드를 담은 이메일을 발송합니다.
     *
     * @param toEmail 수신자 이메일 주소
     */
    //@Async // 비동기 처리를 위한 어노테이션 (현재는 주석 처리됨)
    public void sendVerificationEmail(String toEmail) {
        // 여러 이메일이 섞여 들어올 경우를 대비해 첫 번째 주소만 사용하고 공백을 제거합니다.
        String targetEmail = toEmail.split(",")[0].trim();
        
        System.out.println("DEBUG: 메일 발송 시도 대상 -> [" + targetEmail + "]");

        // 6자리 랜덤 인증 코드를 생성합니다.
        String authCode = String.valueOf((int)(Math.random() * 899999) + 100000);

        // 이메일 메시지를 생성합니다.
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(targetEmail); // 정리된 이메일 주소를 수신자로 설정합니다.
        message.setFrom("chldntn7@gmail.com");  // message.setFrom("yourgmail@gmail.com")불일치시 오류가능성 있음
        message.setSubject("[서비스] 인증 코드");
        message.setText("회원가입 인증 코드입니다: " + authCode);

        try {
            // 이메일을 발송합니다.
            mailSender.send(message);
            // Redis에 인증 코드를 5분 동안 저장합니다.
            redisTemplate.opsForValue().set(AUTH_CODE_PREFIX + targetEmail, authCode, Duration.ofMinutes(5));
            System.out.println("DEBUG: 메일 발송 및 Redis 저장 성공!");
        } catch (Exception e) {
            System.err.println("DEBUG: 메일 발송 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 사용자가 입력한 인증 코드를 검증합니다.
     *
     * @param email 사용자 이메일 주소
     * @param code  사용자가 입력한 인증 코드
     * @return 인증 성공 여부
     */
    public boolean verifyCode(String email, String code) {
        // 검증 시에도 안전하게 이메일 주소를 처리합니다.
        String targetEmail = email.split(",")[0].trim();
        // Redis에서 저장된 인증 코드를 가져옵니다.
        String savedCode = redisTemplate.opsForValue().get(AUTH_CODE_PREFIX + targetEmail);
        
        // 저장된 코드와 입력된 코드가 일치하는지 확인합니다.
        if (savedCode != null && savedCode.equals(code)) {
            // 인증 성공 시, 10분 동안 이메일이 인증되었음을 Redis에 저장합니다.
            redisTemplate.opsForValue().set(VERIFIED_PREFIX + targetEmail, "true", Duration.ofMinutes(10));
            // 사용된 인증 코드는 Redis에서 삭제합니다.
            redisTemplate.delete(AUTH_CODE_PREFIX + targetEmail);
            return true;
        }
        return false;
    }

    /**
     * 해당 이메일이 인증되었는지 확인합니다.
     *
     * @param email 확인할 이메일 주소
     * @return 인증 여부
     */
    public boolean isEmailVerified(String email) {
        String targetEmail = email.split(",")[0].trim();
        // Redis에서 해당 이메일의 인증 상태를 가져옵니다.
        String isVerified = redisTemplate.opsForValue().get(VERIFIED_PREFIX + targetEmail);
        // 인증 상태가 "true"인지 확인합니다.
        return "true".equals(isVerified);
    }
}
