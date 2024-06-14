pipeline {
    agent any
    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
        PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
        // 데이터베이스 환경 변수
        DB_URL = 'jdbc:mariadb://localhost:3306/travel'
        DB_USERNAME = 'root'
        DB_PASSWORD = '1q!1q!'
    }
    stages {
        stage('Checkout') {
            steps {
                // 저장소의 해당 브랜치를 체크아웃합니다.
                git branch: env.BRANCH_NAME, url: 'https://github.com/team-MiniJin/BE.git'
                // Java 버전 확인
                sh 'java -version'
                // Gradle 버전 확인
                sh './gradlew -v'
                // 브랜치 이름 확인
                echo "Building branch: ${env.BRANCH_NAME}"
                // JDBC 연결 테스트
                sh '''
                # MariaDB JDBC 드라이버 경로
                JDBC_DRIVER_PATH="/usr/local/lib/mariadb-java-client-3.3.3.jar"

                # Java 파일 컴파일
                javac -cp .:$JDBC_DRIVER_PATH /usr/local/lib/TestJDBC.java

                # Java 파일 실행
                java -cp .:/usr/local/lib:$JDBC_DRIVER_PATH TestJDBC
                '''
            }
        }
        stage('Build') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'develop') {
                        // 빌드 단계
                        sh './gradlew clean build'
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
        /*
        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                // 배포 단계
                sh './gradlew deploy'
            }
        }
         */
    }
}
