pipeline {
    agent any
    environment {
        TERM = 'xterm'
    }
    stages {
        
        // 1. (권장) 이전 빌드 결과물 정리
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
                junit '**/build/test-results/test/*.xml'
            }
        }
        
        // 3. 코드 품질 검사 (Check)
        // Static Analysis만 실행하려면, 프로젝트에 따라 JacocoTestCoverage, PMD, Checkstyle 등을 실행하도록 조정할 수 있습니다.
        stage('Static Code Analysis') {
            steps {
                script {
                    if (isUnix()) {
                        // test는 건너뛰고, 품질 검사 관련 Gradle Task만 실행하도록 조정
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
                        sh './gradlew build -x test'
                    } else {
                        bat 'gradlew.bat build -x test'
                    }
                }
                archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
            }
        }
        // ... (선택 사항: Dockerize, Deploy)
    }
}