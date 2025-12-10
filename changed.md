# 변경 사항 요약

## `build.gradle`

- **Spotless 플러그인 추가**: 코드 포맷을 검사하고 일관된 스타일을 유지하기 위해 [Spotless](https://github.com/diffplug/spotless) 플러그인을 추가했습니다.
- **Java 포맷 설정**: `googleJavaFormat`을 사용하여 Google의 Java 코드 스타일 가이드를 따르도록 설정했습니다.

## `Jenkinsfile`

- **Jenkins 파이프라인 생성**: 프로젝트를 빌드하고 코드 스타일을 검사하는 자동화된 파이프라인을 생성했습니다.
- **파이프라인 단계**:
    1.  **Checkout**: Git 리포지토리에서 소스 코드를 가져옵니다.
    2.  **Build**: `./gradlew build` 명령어를 사용하여 프로젝트를 컴파일하고 테스트를 실행합니다.
    3.  **Lint and Format Check**: `./gradlew spotlessCheck` 명령어를 사용하여 코드 포맷이 올바른지 검사합니다.
