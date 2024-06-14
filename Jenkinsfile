pipeline {
    agent any
    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
        PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
        // 데이터베이스 환경 변수
        DB_URL = 'jdbc:mariadb://localhost:3306/travel'
        DB_USERNAME = 'root'
        DB_PASSWORD = '1q!1q!'
        // 추가 환경 변수
        JWT_SECRET = 'vmfhaltmskdlstkfkdgodyroqkfwkdbalroqkfwkdbalaaaaaaaaaaaaaaaabbbbb'
        SERVICE_KEY_DE = 'K9eWaIkQyqUUDZPWH+jI2Br1awNS1WaksaOZ6EiUbWTEzpVBeXnhTuPjni4n6auCsAUANYZ5o3Q89TF0sFU4bA=='
        SERVICE_KEY_EN = 'K9eWaIkQyqUUDZPWH%2BjI2Br1awNS1WaksaOZ6EiUbWTEzpVBeXnhTuPjni4n6auCsAUANYZ5o3Q89TF0sFU4bA%3D%3D'
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
                #!/bin/bash

                # MariaDB connection details
                DB_HOST="localhost"
                DB_PORT="3306"
                DB_NAME="travel"
                DB_USER="root"
                DB_PASSWORD="1q!1q!"

                # JDBC URL
                JDBC_URL="jdbc:mariadb://${DB_HOST}:${DB_PORT}/${DB_NAME}"

                # SQL query to test connection
                SQL_QUERY="SELECT 1;"

                # Java code to test JDBC connection
                JAVA_CODE=$(cat <<EOF
                import java.sql.Connection;
                import java.sql.DriverManager;
                import java.sql.ResultSet;
                import java.sql.Statement;

                public class TestJDBC {
                    public static void main(String[] args) {
                        try {
                            Class.forName("org.mariadb.jdbc.Driver");
                            Connection conn = DriverManager.getConnection("$JDBC_URL", "$DB_USER", "$DB_PASSWORD");
                            Statement stmt = conn.createStatement();
                            ResultSet rs = stmt.executeQuery("$SQL_QUERY");
                            while (rs.next()) {
                                System.out.println("JDBC Connection Successful: " + rs.getInt(1));
                            }
                            rs.close();
                            stmt.close();
                            conn.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.exit(1);
                        }
                    }
                }
                EOF
                )

                # Save Java code to file
                echo "$JAVA_CODE" > TestJDBC.java

                # Compile Java code
                javac -cp /usr/local/lib/mariadb-java-client-3.3.3.jar TestJDBC.java

                # Run Java code
                java -cp .:/usr/local/lib/mariadb-java-client-3.3.3.jar TestJDBC
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
