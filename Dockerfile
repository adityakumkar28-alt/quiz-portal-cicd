FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/quiz-portal.jar /app/quiz-portal.jar

EXPOSE 8080

ENV APP_VERSION=1.0.0

ENTRYPOINT ["java", "-jar", "/app/quiz-portal.jar"]
