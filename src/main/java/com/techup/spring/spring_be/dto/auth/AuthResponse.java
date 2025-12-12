package com.techup.spring.spring_be.dto.auth;

import com.techup.spring.spring_be.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private String accessToken;
    private Long id;
    private String email;
    private String name;
    private UserRole role;
    private String profileImage;
    private String communityName;

}
