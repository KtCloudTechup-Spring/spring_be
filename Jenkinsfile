pipeline {
    agent any
    
    environment {
        TERM = 'xterm'
        // Gradle 캐시 경로 설정
        GRADLE_USER_HOME = '.gradle-cache'
    }
    
    stages {
        // 1. 빌드 준비 (실행 권한 부여)
        stage('Prepare') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'chmod +x gradlew'
                    }
                }
            }
        }
        
        // 2. 이전 빌드 결과 정리
        stage('Clean') {
            steps {
                script {
                    if (isUnix()) {
                        sh './gradlew clean'
                    } else {
                        bat 'gradlew.bat clean'
                    }
                }
            }
        }
        
        // 3. 빌드 및 패키징 (핵심: -x test 옵션으로 DB 연결이 필요한 모든 테스트 생략)
        stage('Build & Package') {
            steps {
                script {
                    echo 'Building application without running tests (skipping DB check)...'
                    if (isUnix()) {
                        // -x test: 테스트 태스크 제외
                        // -x check: 정적 분석 및 기타 검증 태스크 제외
                        sh './gradlew build -x test -x check'
                    } else {
                        bat 'gradlew.bat build -x test -x check'
                    }
                }
                // 생성된 JAR 파일 보관
                archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline finished.'
            cleanWs() // 워크스페이스 정리
        }
        success {
            echo '빌드 성공! (데이터베이스 확인 없이 패키징 완료)'
        }
        failure {
            echo '빌드 실패! 로그를 확인하세요.'
        }
    }
}
