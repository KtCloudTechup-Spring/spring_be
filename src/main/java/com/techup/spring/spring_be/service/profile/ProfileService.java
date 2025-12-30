package com.techup.spring.spring_be.service.profile;

import com.techup.spring.spring_be.domain.User;
import com.techup.spring.spring_be.dto.user.ChangeProfileRequest;
import com.techup.spring.spring_be.dto.user.UserResponse;
import com.techup.spring.spring_be.repository.UserRepository;
import com.techup.spring.spring_be.service.storage.S3FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProfileService {

    private final UserRepository userRepository;
    private final S3FileStorageService s3FileStorageService;

    @Transactional
    public UserResponse changeProfile(String email, ChangeProfileRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

        // 값 변경
        user.setName(request.getName());

        return new UserResponse(
                user.getEmail(),        // 기존 값
                user.getName(),         // 변경된 값
                user.getRole(),
                user.getProfileImage(),
                user.getCommunity().getName()
        );

    }

    @Transactional
    public UserResponse saveAvatar(User user, MultipartFile image) throws IOException {
        // aws s3에 이미지 저장
        String imageUrl = s3FileStorageService.uploadPostImage(image);

        user.setProfileImage(imageUrl);
        userRepository.save(user);

        return new UserResponse(
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getProfileImage(),
                user.getCommunity().getName()
        );





    }


}
