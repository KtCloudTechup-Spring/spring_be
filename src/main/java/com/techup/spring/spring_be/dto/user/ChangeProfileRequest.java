package com.techup.spring.spring_be.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeProfileRequest {
    @NotBlank
    private String name;
}
