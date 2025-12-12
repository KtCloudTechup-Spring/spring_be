package com.techup.spring.spring_be.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterRequest {
    private String email;
    private String password;
    private String name;

    //private Long communityId;
    @JsonProperty("community_id")
    private Long communityId;

    private String profileImage;
}