pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/KtCloudTechup-Spring/spring_be.git', branch: 'dev'
            }
        }

        stage('Build') {
            steps {
                script {
                    if (isUnix()) {
                        sh './gradlew build'
                    } else {
                        bat 'gradlew.bat build'
                    }
                }
            }
        }

        stage('Lint and Format Check') {
            steps {
                script {
                    if (isUnix()) {
                        sh './gradlew spotlessCheck'
                    } else {
                        bat 'gradlew.bat spotlessCheck'
                    }
                }
            }
        }
    }
}