# Online Quiz & Exam Portal

Production-ready examination and assessment portal built with Java 17, Spring Boot 3.2.5, and MySQL 8. Designed for reliable execution across local environments and Jenkins CI/CD pipelines.

## Technology Stack

- Language: Java 17 (OpenJDK)
- Framework: Spring Boot 3.2.5
- Persistence: Spring Data JPA (Hibernate 6)
- Database: MySQL 8 (Production/Local), H2 In-Memory (Test scope for CI isolation)
- Build Tool: Apache Maven 3.9+
- Containerization: Docker (Eclipse Temurin JRE 17 Alpine)
- Orchestration: Docker Compose
- CI/CD: Jenkins Declarative Pipeline (Windows Agent)
- Security: Aquasec Trivy Container Vulnerability Scanner
- Testing: JUnit 5, Spring Boot Test, Maven Surefire Plugin 3.2.5
- Frontend: Responsive Vanilla HTML5, CSS3, JavaScript (Dark Mode, Inter typography)

## Architecture Overview

- `QuizPortalApplication`: Spring Boot main entry point with automated CommandLineRunner database seeder (initializes 5 Software Engineering & DevOps questions if table is empty).
- `QuizQuestion`: JPA entity representing multiple-choice questions with 4 options and mark allocations.
- `ExamAttempt`: JPA entity persisting candidate submissions, breakdown statistics (correct, wrong, skipped), completion time, and final status.
- `QuizScoringService`: Business logic service for scoring, time validity checking, and pass/fail/timeout evaluation.
- `QuizController`: REST API endpoints for metadata, question management, candidate submissions, and historical attempt queries.
- `index.html`: Dark-mode single page application featuring real-time countdown timer, quiz interface, question bank manager, and leaderboard.

## Scoring & Time Rules

- Correct Answer: +4 marks
- Incorrect Answer: -1 mark
- Unattempted (0): 0 marks
- Minimum Floor: Clamped at 0 marks (negative total score is not possible)
- Allowed Time: 120 seconds (02:00) with a 5-second network grace buffer
- Pass Threshold: >= 40.0% of total possible marks
- Status Outcomes:
  - `PASS`: Score percentage >= 40% and submitted within allowed time + grace buffer
  - `FAIL`: Score percentage < 40% and submitted within allowed time + grace buffer
  - `TIMEOUT`: Submission exceeds allowed time + 5-second grace buffer

## Configuration & Environment Variables

| Variable | Description | Default Fallback |
|---|---|---|
| `DB_URL` | MySQL JDBC URL | `jdbc:mysql://localhost:3306/quizdb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true` |
| `DB_USER` | MySQL Username | `root` |
| `DB_PASS` | MySQL Password | `rootpassword` |
| `APP_VERSION` | Application Build Version | `v1.0-local` |
| `SERVER_PORT` | HTTP Server Port | `8080` |

Unit and integration tests run independently against an embedded H2 database (`jdbc:h2:mem:quiztestdb`), requiring no running MySQL instance during `mvn clean test`.

## Build, Test, and Containerization Commands

### 1. Run Tests Locally
```bash
mvn clean test
```
JUnit XML reports are generated in `target/surefire-reports/`.

### 2. Package JAR
```bash
mvn clean package -DskipTests
```
Generates `target/quiz-portal.jar`.

### 3. Docker Network & MySQL 8 Provisioning
```bash
docker network create quiz-net
docker run -d --name mysql-quiz-db --network quiz-net -e MYSQL_ROOT_PASSWORD=rootpassword -e MYSQL_DATABASE=quizdb -p 3307:3306 --restart unless-stopped mysql:8.0
```

### 4. Build and Run Application Container
```bash
docker build -t quiz-portal:local .
docker run -d --name quiz-portal-app --network quiz-net -p 8085:8080 \
  -e DB_URL="jdbc:mysql://mysql-quiz-db:3306/quizdb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true" \
  -e DB_USER=root \
  -e DB_PASS=rootpassword \
  -e APP_VERSION=v1.0-docker \
  quiz-portal:local
```

### 5. Multi-Container Orchestration via Docker Compose
```bash
docker compose up -d
```
Access the application on host port `8085` (`http://localhost:8085`).

## Jenkins CI/CD Pipeline Configuration

The declarative `Jenkinsfile` is tailored for Windows nodes with Maven and Docker Desktop integration.

### Pipeline Stages
1. **Checkout SCM**: Clones source repository.
2. **Build and Unit Test**: Executes `mvn clean test` and records test reports via JUnit plugin (`**/target/surefire-reports/*.xml`).
3. **Package Application**: Generates runnable JAR artifact with `mvn package -DskipTests`.
4. **Build Docker Image**: Builds Docker image tagged with `${env.BUILD_NUMBER}` and `latest`.
5. **Trivy Vulnerability Scan**: Scans image for HIGH and CRITICAL vulnerabilities via `aquasec/trivy:latest`.
6. **Push to Docker Hub**: Authenticates and publishes images to `adityakumkar/quiz-portal`.
7. **Deploy Container**: Manages container lifecycle on `quiz-net` with persistent MySQL connection.

### Required Jenkins Credentials
- `dockerhub-creds`: Username & Password credential for Docker Hub registry (`adityakumkar`).
- `mysql-db-creds`: Username & Password credential for MySQL database (`root` / `rootpassword`).

## REST API Reference

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/meta` | Build version, question count, and scoring parameters |
| `GET` | `/api/questions` | List all available quiz questions |
| `POST` | `/api/questions` | Create a new question in the question bank |
| `DELETE` | `/api/questions/{id}` | Remove a question by ID |
| `POST` | `/api/submit` | Submit candidate answers, calculate score, and persist attempt |
| `GET` | `/api/attempts` | Retrieve all historical exam attempts ordered by latest first |
