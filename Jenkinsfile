pipeline {
    agent any
    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
        PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
        // 데이터베이스 환경 변수
        DB_URL = 'jdbc:mariadb://localhost:3306/travel'
        DB_USERNAME = 'root'
        DB_PASSWORD = '1q!1q!'
        JENKINS_SERVER = "172.17.0.2"
        NGINX_MINIJIN = "172.17.0.3"
        BUILD_PJASYPT = credentials('Pjasypt')
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
                # 작업 디렉토리
                WORK_DIR="/tmp"

                # Java 파일 컴파일
                javac -cp .:$JDBC_DRIVER_PATH $WORK_DIR/TestJDBC.java

                # Java 파일 실행
                java -cp .:$WORK_DIR:$JDBC_DRIVER_PATH TestJDBC
                '''
            }
        }
        stage('Build') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'develop') {
                        // 빌드 단계
                        sh "./gradlew clean build ${BUILD_PJASYPT}"
                    }
                }
            }
        }
        stage('Test') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'develop' || env.BRANCH_NAME.startsWith('feature/')) {
                        // 테스트 단계
                        sh "./gradlew test ${BUILD_PJASYPT}"
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
            steps {
                script {
                    if (env.BRANCH_NAME == 'develop' || env.BRANCH_NAME.contains('jenkins')) {
                        sshagent (credentials: ['jenkins-ssh-key']) {
                            try {
                                sh '''
                                echo "Starting SSH connection..."
                                ssh -vvv -o StrictHostKeyChecking=no root@${NGINX_MINIJIN} 'echo "SSH connection successful"'
                                echo "SSH connection established successfully."
                                echo "Killing 8080, java service"
                                ssh -o StrictHostKeyChecking=no root@172.17.0.3 << 'EOF'
                                PIDS=$(lsof -t -i:8080)
                                if [ -n "$PIDS" ]; then
                                  echo "Killing processes using port 8080: $PIDS"
                                  for PID in $PIDS; do
                                    kill -9 $PID || echo "Failed to kill process $PID"
                                  done
                                else
                                  echo "No process found using port 8080"
                                fi
                                pkill -f "java -jar /home/user/travel-0.0.1-SNAPSHOT.jar" || true
                                EOF
                                echo "Killing Complete 8080, java service"
                                echo "Transferring file..."
                                scp -v /var/jenkins_home/workspace/minijin_BE_develop/build/libs/travel-0.0.1-SNAPSHOT.jar root@${NGINX_MINIJIN}:/home/user/
                                echo "File transferred successfully."
                                echo "Deploying the application..."
                                '''
                                ssh -o StrictHostKeyChecking=no root@${NGINX_MINIJIN} "nohup java ${BUILD_PJASYPT} -jar /home/user/travel-0.0.1-SNAPSHOT.jar --server.port=8080 > /home/user/travel.log 2>&1 &"
                                echo "Application deployed successfully."

                            } catch (Exception e) {
                                echo "SSH connection or file transfer failed: ${e}"g
                                error "Stopping pipeline due to SSH failure"
                            }
                        }
                    }
                }
            }
        }
    }
}