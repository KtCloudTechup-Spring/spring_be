# Swagger (Springdoc OpenAPI) 적용 가이드

이 문서는 Spring Boot 프로젝트에 Swagger UI (Springdoc OpenAPI)를 적용하는 방법을 설명합니다. 다른 브랜치에 적용하거나 변경 사항을 이해하는 데 도움이 됩니다.

## 1. 개요

API 문서를 자동으로 생성하고 테스트할 수 있는 Swagger UI를 프로젝트에 통합했습니다. 이를 통해 개발자는 API 엔드포인트를 쉽게 확인하고 상호작용할 수 있습니다.

## 2. 변경 사항

### 2.1. `build.gradle` 파일

`build.gradle` 파일에 `springdoc-openapi-starter-webmvc-ui` 의존성을 추가하여 Springdoc OpenAPI 라이브러리를 포함했습니다.

**중요**: 기존 Spring Boot 버전(`3.5.9-SNAPSHOT`)이 불안정한 스냅샷 버전으로 인해 Swagger 관련 오류가 발생하여, 안정적인 버전인 **`3.2.5`**로 업데이트되었습니다.

**변경 및 추가된 의존성:**

```gradle
dependencies {
    // ... 기존 의존성 ...
    implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0'
}
```

### 2.2. `src/main/java/com/techup/spring/spring_be/config/SecurityConfig.java` 파일

Spring Security가 Swagger UI 및 API 문서 경로에 대한 접근을 허용하도록 `SecurityConfig.java` 파일을 수정했습니다. `SecurityFilterChain` 빈에서 `permitAll()` 메서드에 Swagger 관련 경로를 추가했습니다.

**변경 내용:**

```java
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // ✅ 인증 없이 가능
                        .requestMatchers(
                                "/api/health",
                                "/api/login",
                                "/api/register",
                                // 👇 추가된 Swagger 관련 경로
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/webjars/**"
                        ).permitAll()

                        // ✅ 조회는 공개
                        .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/posts/*/comments").permitAll() // 댓글 조회 엔드포인트가 이 형태면

                        // 🔒 그 외는 인증 필요
                        .requestMatchers("/api/profile/**").authenticated()
                        .requestMatchers("/api/posts/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```

## 3. Swagger UI 접근 방법

애플리케이션을 실행한 후, 웹 브라우저에서 다음 URL로 접속하여 Swagger UI를 확인할 수 있습니다:

`http://localhost:8080/swagger-ui.html`

(만약 애플리케이션 포트가 다르다면 `8080` 대신 해당 포트 번호를 사용하세요.)

## 4. API 문서 상세화 (프론트엔드 이해도 향상)

프론트엔드 개발자가 API 문서를 더 쉽게 이해하고 활용할 수 있도록, Springdoc OpenAPI에서 제공하는 어노테이션들을 사용하여 API의 세부 정보를 풍부하게 추가했습니다.

### 주요 어노테이션:

*   **`@Tag` (클래스 레벨):** 컨트롤러 클래스에 붙여 API 그룹의 이름과 설명을 정의합니다.
*   **`@Operation` (메서드 레벨):** 각 엔드포인트(메서드)에 붙여 해당 API의 요약(`summary`), 상세 설명(`description`)을 제공합니다.
*   **`@Parameter` (파라미터 레벨):** 특정 요청 매개변수(`@PathVariable`, `@RequestParam`, `@RequestBody` 등)에 대한 설명을 추가합니다. `hidden = true`를 사용하여 Swagger UI에서 특정 파라미터를 숨길 수도 있습니다.
*   **`@ApiResponse` (메서드 레벨):** API 호출 시 가능한 응답 코드(예: 200, 400, 401)별로 어떤 응답이 반환되는지 설명합니다. `content = @Content(schema = @Schema(implementation = YourResponseDto.class))`와 같이 응답 DTO를 명시할 수 있습니다. 에러 응답의 경우 프로젝트에 이미 존재하는 `ErrorResponse.class`를 사용할 수 있습니다.
*   **`@Schema` (DTO 클래스 또는 필드 레벨):** DTO 클래스나 필드에 붙여 데이터 모델의 구조와 필드별 설명을 제공합니다. `example` 속성을 사용하여 예시 값을 보여줄 수 있고, `requiredMode`를 통해 필수의 여부를 명시할 수 있습니다.

### 4.1. `src/main/java/com/techup/spring/spring_be/controller/PostController.java` 변경 사항

`PostController`에 `@Tag` 어노테이션을 추가하고, `createPost` 메서드에 `@Operation` 및 `@ApiResponse` 어노테이션을 추가하여 API의 상세 정보를 명시했습니다. 또한 `UserDetails` 파라미터는 Swagger UI에서 숨겼습니다.

**적용된 코드:**

```java
package com.techup.spring.spring_be.controller;

import com.techup.spring.spring_be.domain.User;
import com.techup.spring.spring_be.dto.common.ApiResponse;
import com.techup.spring.spring_be.dto.post.PostCreateRequest;
import com.techup.spring.spring_be.dto.post.PostResponse;
import com.techup.spring.spring_be.dto.post.PostUpdateRequest;
import com.techup.spring.spring_be.repository.UserRepository;
import com.techup.spring.spring_be.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
// import io.swagger.v3.oas.annotations.responses.ApiResponse; // 이름 충돌 방지를 위해 풀패키지명 사용
// import io.swagger.v3.oas.annotations.tags.Tag; // 이름 충돌 방지를 위해 풀패키지명 사용
import com.techup.spring.spring_be.dto.common.ErrorResponse;

@io.swagger.v3.oas.annotations.tags.Tag(name = "게시글", description = "게시글과 관련된 모든 API 엔드포인트들을 제공합니다.") // 클래스 레벨
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    private Long getCurrentUserId(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("회원 정보를 찾을 수 없습니다."));
        return user.getId();
    }

    /** 게시글 생성 (로그인 필요) */
    @Operation(summary = "새로운 게시글 생성", description = "인증된 사용자가 특정 커뮤니티에 새로운 게시글을 생성합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "게시글이 성공적으로 생성되었으며, 생성된 게시글 정보를 반환합니다.",
            content = @Content(schema = @Schema(implementation = PostResponse.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "요청 본문(RequestBody)의 데이터가 유효하지 않습니다.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다. 유효한 JWT 토큰이 필요합니다.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ApiResponse<PostResponse> createPost(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails, // Swagger UI에서 이 파라미터를 숨김
            @Valid @RequestBody PostCreateRequest request
    ) {
        Long userId = getCurrentUserId(userDetails);
        PostResponse res = postService.createPost(userId, request);
        return ApiResponse.ok("게시글 생성 성공", res);
    }

    // ... (나머지 코드) ...
}
```

### 4.2. `src/main/java/com/techup/spring/spring_be/dto/post/PostCreateRequest.java` 변경 사항

`PostCreateRequest` DTO에 `@Schema` 어노테이션을 추가하여 DTO 자체의 설명과 각 필드의 설명, 예시 값, 필수 여부 등을 명시했습니다.

**적용된 코드:**

```java
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
```

### 4.3. 컴파일 오류 수정: `ApiResponse` 이름 충돌 해결

Springdoc의 `@ApiResponse` 어노테이션과 프로젝트의 `ApiResponse` DTO 이름 충돌로 인한 컴파일 오류를 해결했습니다. `PostController.java`에서 Springdoc의 `@ApiResponse` 어노테이션을 풀패키지 이름으로 변경하여 사용합니다.

**`src/main/java/com/techup/spring/spring_be/controller/PostController.java` 변경 사항:**

```java
// 기존 import io.swagger.v3.oas.annotations.responses.ApiResponse; 라인 삭제

// @ApiResponse 대신 아래와 같이 풀패키지 경로로 사용
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "게시글 생성 성공",
            content = @Content(schema = @Schema(implementation = PostResponse.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 데이터",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
```

### 4.4. 컴파일 오류 수정: `PostCreateRequest` 필드 접근 방식 변경 (getter 사용)

이전에 `PostCreateRequest`가 Java `record` 타입으로 잘못 인지되어 필드에 직접 접근(`request.title()`)하도록 변경했었습니다. 하지만 `PostCreateRequest`는 `lombok.Getter`를 사용하는 일반 `class`이므로, 필드 접근 시 getter 메서드(`request.getTitle()`)를 사용해야 합니다. 이에 따라 `PostService.java`의 코드를 다시 getter를 사용하도록 수정했습니다.

**`src/main/java/com/techup/spring/spring_be/service/PostService.java` 변경 사항:**

```java
// 변경 후
Community community = communityRepository.findById(request.getCommunityId())
                .orElseThrow(() -> new EntityNotFoundException("커뮤니티가 존재하지 않습니다."));

Post post = new Post(user, community, request.getTitle(), request.getContent());
```

## 5. 최종 확인 요청

위의 모든 변경 사항(Swagger 기본 설정, API 문서 상세화, 그리고 발생했던 컴파일 오류 수정)이 올바르게 적용되었는지 확인하려면, **프로젝트를 다시 빌드하고 애플리케이션을 재실행해 주십시오.**

그 후에 웹 브라우저에서 `http://localhost:8080/swagger-ui.html` 로 접속하여 다음을 확인해 주시기 바랍니다:
1.  **애플리케이션 정상 실행**: 컴파일 오류 없이 애플리케이션이 정상적으로 시작되는지.
2.  **Swagger UI 접근**: Swagger UI 페이지에 정상적으로 접근되는지.
3.  **API 문서 상세화 확인**: `PostController`의 `createPost` API 문서를 클릭하여 추가된 설명, 예시, 응답 DTO 정보 등이 Swagger UI에 풍부하게 반영되었는지 확인합니다. 특히 `PostCreateRequest`의 필드 설명도 잘 나오는지 확인해주세요.

