pipeline {
    agent any

    environment {
        // 환경 변수를 정의할 수 있습니다.
    }

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
                        sh 'make build'
                    }
                }
            }
        }
        stage('Test') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'develop' || env.BRANCH_NAME.startswith('feature/')) {
                        // 테스트 단계
                        sh 'make test'
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
                sh 'make release'
            }
        }
        stage('Hotfix') {
            when {
                branch 'hotfix/*'
            }
            steps {
                // 핫픽스 단계
                sh 'make hotfix'
            }
        }
        stage('Deploy') {
            when {
                branch 'master'
            }
            steps {
                // 배포 단계
                sh 'make deploy'
            }
        }
    }
}
