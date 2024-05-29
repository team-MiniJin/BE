pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                // 저장소의 해당 브랜치를 체크아웃합니다.
                git branch: env.BRANCH_NAME, url: 'https://github.com/team-MiniJin/BE.git'
            }
        }
        stage('Build') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'develop' || env.BRANCH_NAME.startsWith('feature/')) {
                        // 빌드 단계
                        sh './gradlew build'
                    }
                }
            }
        }
        stage('Test') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'develop' || env.BRANCH_NAME.startsWith('feature/')) {
                        // 테스트 단계
                        sh './gradlew test'
                    }
                }
            }
        }
        stage('Release') {
            when {
                branch 'release/*'
            }
            steps {
                // 릴리스 단계
                sh './gradlew publish'
            }
        }
        stage('Hotfix') {
            when {
                branch 'hotfix/*'
            }
            steps {
                // 핫픽스 단계
                sh './gradlew build'
            }
        }
        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                // 배포 단계
                sh './gradlew deploy'
            }
        }
    }
}
