package com.techup.spring.spring_be.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentCreateRequest {

    @NotBlank
    private String content;
}
