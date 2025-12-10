pipeline {
    agent any
    // 확인
    // 환경 변수 설정: 캐싱 경로 지정 및 터미널 설정
    environment {
        TERM = 'xterm'
        // Gradle 의존성 캐싱 경로를 워크스페이스 내부로 설정하여 캐싱을 활성화합니다.
        GRADLE_USER_HOME = '.gradle-cache'
    }
    
    stages {
        
        // 0. 빌드 준비 (gradlew 실행 권한 부여)
        // 이전 빌드에서 발생한 'Permission denied' 문제를 해결하기 위해 추가합니다.
        stage('Prepare for Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'chmod +x gradlew'
                    }
                    // Windows (bat) 환경에서는 기본적으로 실행 권한 문제가 발생하지 않으므로 생략합니다.
                }
            }
        }
        
        // 1. 이전 빌드 결과물 정리
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
        
        // 2. 단위 테스트 실행 (Test)
        stage('Unit Test') {
            steps {
                script {
                    if (isUnix()) {
                        sh './gradlew test'
                    } else {
                        bat 'gradlew.bat test'
                    }
                }
                // JUnit 테스트 결과를 Jenkins에 게시하여 시각화합니다.
                junit '**/build/test-results/test/*.xml'
            }
        }
        
        // 3. 코드 품질 검사 (Check)
        stage('Static Code Analysis') {
            steps {
                script {
                    if (isUnix()) {
                        // 테스트는 Unit Test 스테이지에서 이미 실행했으므로 건너뛰고 품질 검사 관련 태스크만 실행합니다.
                        sh './gradlew check -x test' 
                    } else {
                        bat 'gradlew.bat check -x test'
                    }
                }
            }
        }
        
        // 4. 빌드 및 패키징 (Build & Package)
        stage('Build & Package') {
            steps {
                script {
                    if (isUnix()) {
                        // 최종 JAR 파일을 생성하며, 테스트는 건너뜁니다.
                        sh './gradlew build -x test'
                    } else {
                        bat 'gradlew.bat build -x test'
                    }
                }
                // 생성된 JAR 파일을 Jenkins 워크스페이스에 보관합니다.
                archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
            }
        }
    } // end of stages
    
    // 빌드 후 처리 블록 (안정성 및 유지보수성 강화)
    post {
        // 빌드의 성공/실패 여부와 관계없이 항상 실행
        always {
            echo 'Pipeline finished.'
            // 워크스페이스 정리: 빌드 후 생성된 파일(빌드된 JAR 제외)과 임시 파일을 정리하여 디스크 공간을 확보합니다.
            cleanWs()
        }
        // 빌드가 성공했을 때 실행
        success {
            echo 'Pipeline succeeded! Ready for deployment.'
            // TODO: 성공 알림 로직 추가 (예: Slack 알림 전송)
        }
        // 빌드 중 어떤 단계라도 실패했을 때 실행
        failure {
            echo 'Pipeline failed! Check logs for errors.'
            // TODO: 실패 알림 로직 추가 (예: 이메일 또는 Slack 알림 전송))
        }
        // 이 외에도 unstable, aborted 등 다양한 상태에 따라 후처리 로직을 추가할 수 있습니다.
    }
}  
