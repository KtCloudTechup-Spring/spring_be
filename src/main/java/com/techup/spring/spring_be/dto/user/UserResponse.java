package com.techup.spring.spring_be.dto.user;

import com.techup.spring.spring_be.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private String email;
    private String name;
    private UserRole role;
    private String profileImage;
    private String communityName;
}
