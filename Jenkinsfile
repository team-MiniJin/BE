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
        DEPLOY_DJASYPT = credentials('Djasypt')
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
            when { branch 'develop' }
            steps {
                sh "./gradlew clean build ${BUILD_PJASYPT}"
            }
        }
        stage('Test') {
            when { branch pattern: 'develop|feature/.*' }
            steps {
                sh "./gradlew test ${BUILD_PJASYPT}"
            }
        }
        stage('Release') {
            when { branch 'release/*' }
            steps {
                sh './gradlew publish'
            }
        }
        stage('Hotfix') {
            when { branch 'hotfix/*' }
            steps {
                sh './gradlew build'
            }
        }
        stage('Deploy') {
            when {
                anyOf {
                    branch 'develop'
                    changeRequest()
                }
            }
            steps {
                script {
                    echo "Using SSH credentials: jenkins-ssh-key"
                    sshagent (credentials: ['jenkins-ssh-key']) {
                        try {
                            sh """
                            echo "Starting SSH connection..."
                            ssh -i ~/.ssh/jenkins_agent_key root@$NGINX_MINIJIN 'echo "SSH connection successful"'
                            echo "SSH connection established successfully."
                            echo "Killing 8080, java service"
                            ssh -i ~/.ssh/jenkins_agent_key root@$NGINX_MINIJIN '/home/user/kill_java.sh'
                            echo "Killing Complete 8080, java service"
                            echo "Transferring file..."
                            scp -i ~/.ssh/jenkins_agent_key /var/jenkins_home/workspace/minijin_BE_develop/build/libs/travel-0.0.1-SNAPSHOT.jar root@${env.NGINX_MINIJIN}:/home/user/
                            echo "File transferred successfully."
                            echo "Deploying the application..."
                            ssh -i ~/.ssh/jenkins_agent_key -o StrictHostKeyChecking=no root@${env.NGINX_MINIJIN} "nohup java ${env.DEPLOY_DJASYPT} -jar /home/user/travel-0.0.1-SNAPSHOT.jar --server.port=8080 > /home/user/travel.log 2>&1 &"
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