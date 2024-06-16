pipeline {
    agent any
    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
        PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
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
                git branch: env.BRANCH_NAME, url: 'https://github.com/team-MiniJin/BE.git'
                sh 'java -version'
                sh './gradlew -v'
                echo "Building branch: ${env.BRANCH_NAME}"
                sh '''
                JDBC_DRIVER_PATH="/usr/local/lib/mariadb-java-client-3.3.3.jar"
                WORK_DIR="/tmp"
                javac -cp .:$JDBC_DRIVER_PATH $WORK_DIR/TestJDBC.java
                java -cp .:$WORK_DIR:$JDBC_DRIVER_PATH TestJDBC
                '''
            }
        }
        stage('Build') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'develop') {
                        sh "./gradlew clean build ${BUILD_PJASYPT}"
                    }
                }
            }
        }
        stage('Test') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'develop' || env.BRANCH_NAME.startsWith('feature/')) {
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
                sh './gradlew publish'
            }
        }
        stage('Hotfix') {
            when {
                branch 'hotfix/*'
            }
            steps {
                sh './gradlew build'
            }
        }
        stage('Deploy') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'develop' || env.BRANCH_NAME.contains('jenkins')) {
                        echo "Using SSH credentials: jenkins-ssh-key"
                        sshagent (credentials: ['jenkins-ssh-key']) {
                            try {
                                sshCommand remote: [name: 'nginx-minijin', host: "${NGINX_MINIJIN}", user: 'root', allowAnyHosts: true], command: """
                                  echo "Killing 8080, java service"
                                  /home/user/kill_java.sh
                                  echo "Killing Complete 8080, java service"
                                """
                                sshPut remote: [name: 'nginx-minijin', host: "${NGINX_MINIJIN}", user: 'root', allowAnyHosts: true], from: 'build/libs/travel-0.0.1-SNAPSHOT.jar', into: '/home/user/'

                                sshCommand remote: [name: 'nginx-minijin', host: "${NGINX_MINIJIN}", user: 'root', allowAnyHosts: true], command: """
                                  echo "Deploying the application..."
                                  nohup java ${env.BUILD_PJASYPT} -jar /home/user/travel-0.0.1-SNAPSHOT.jar --server.port=8080 > /home/user/travel.log 2>&1 &
                                  echo "Application deployed successfully."
                                """

                            } catch (Exception e) {
                                echo "SSH connection or file transfer failed: ${e}"
                                error "Stopping pipeline due to SSH failure"
                            }
                        }
                    }
                }
            }
        }
    }
}
