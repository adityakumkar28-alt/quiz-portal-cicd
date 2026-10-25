pipeline {
    agent any

    environment {
        IMAGE_NAME = "adityakumkar/quiz-portal"
        IMAGE_TAG = "${env.BUILD_NUMBER}"
        CONTAINER_NAME = "quiz-portal-app"
        NETWORK_NAME = "quiz-net"
        MYSQL_CONTAINER = "mysql-quiz-db"
        HOST_PORT = "8085"
        MAVEN_BIN = "C:\\apache-maven-3.9.6\\bin\\mvn.cmd"
    }

    stages {
        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Build and Unit Test') {
            steps {
                bat "${MAVEN_BIN} clean test"
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package Application') {
            steps {
                bat "${MAVEN_BIN} package -DskipTests"
            }
        }

        stage('Build Docker Image') {
            steps {
                bat "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Trivy Vulnerability Scan') {
            steps {
                bat "docker run --rm -v /var/run/docker.sock:/var/run/docker.sock -v trivy-cache:/root/.cache/ aquasec/trivy:latest image --pkg-types os --scanners vuln --skip-db-update --severity HIGH,CRITICAL --exit-code 0 --no-progress ${IMAGE_NAME}:${IMAGE_TAG}"
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    bat "echo %DOCKER_PASS%| docker login -u %DOCKER_USER% --password-stdin"
                    bat "docker push ${IMAGE_NAME}:${IMAGE_TAG}"
                    bat "docker push ${IMAGE_NAME}:latest"
                    bat "docker logout"
                }
            }
        }

        stage('Deploy Container') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'mysql-db-creds', usernameVariable: 'DB_USER_CRED', passwordVariable: 'DB_PASS_CRED')]) {
                    bat "docker network inspect %NETWORK_NAME% >nul 2>&1 || docker network create %NETWORK_NAME%"
                    bat "docker inspect %MYSQL_CONTAINER% >nul 2>&1 || docker run -d --name %MYSQL_CONTAINER% --network %NETWORK_NAME% -e MYSQL_ROOT_PASSWORD=%DB_PASS_CRED% -e MYSQL_DATABASE=quizdb -p 3307:3306 --restart unless-stopped mysql:8.0"
                    bat "docker rm -f %CONTAINER_NAME% >nul 2>&1 || exit /b 0"
                    bat """
docker run -d --name ${CONTAINER_NAME} --network ${NETWORK_NAME} -p ${HOST_PORT}:8080 ^
  -e DB_URL="jdbc:mysql://${MYSQL_CONTAINER}:3306/quizdb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true" ^
  -e DB_USER="%DB_USER_CRED%" ^
  -e DB_PASS="%DB_PASS_CRED%" ^
  -e APP_VERSION="Build-#${BUILD_NUMBER}" ^
  --restart unless-stopped ${IMAGE_NAME}:${IMAGE_TAG}
"""
                }
            }
        }
    }
}
