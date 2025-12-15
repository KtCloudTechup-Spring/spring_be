package com.techup.spring.spring_be.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "새로운 게시글을 생성하기 위한 요청 본문(Request Body) DTO입니다.")
@Getter
@NoArgsConstructor
public class PostCreateRequest {
    @Schema(description = "게시글이 속할 커뮤니티의 고유 ID입니다.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Long communityId;

    @Schema(description = "게시글의 제목입니다.", example = "새로운 게시글 제목 예시", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String title;

    @Schema(description = "게시글의 내용입니다.", example = "여기에는 작성할 게시글의 상세 내용이 들어갑니다.", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String content;
}

