package com.techup.spring.spring_be.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateRequest {

    @NotNull
    private Long communityId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;
}
