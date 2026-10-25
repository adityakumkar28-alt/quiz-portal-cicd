FROM eclipse-temurin:17-jre-alpine AS unpacker
WORKDIR /unpacked
COPY target/quiz-portal.jar app.jar
RUN (jar -xf app.jar 2>/dev/null || unzip -q app.jar) && rm app.jar && \
    mkdir -p /layers/lib1 /layers/lib2 /layers/lib3 /layers/lib4 && \
    find BOOT-INF/lib -type f -name "[a-f]*.jar" -exec mv {} /layers/lib1/ \; && \
    find BOOT-INF/lib -type f -name "[g-l]*.jar" -exec mv {} /layers/lib2/ \; && \
    find BOOT-INF/lib -type f -name "[m-r]*.jar" -exec mv {} /layers/lib3/ \; && \
    find BOOT-INF/lib -type f -exec mv {} /layers/lib4/ \;

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=unpacker /unpacked/META-INF /app/META-INF
COPY --from=unpacker /unpacked/org /app/org
COPY --from=unpacker /layers/lib1/ /app/BOOT-INF/lib/
COPY --from=unpacker /layers/lib2/ /app/BOOT-INF/lib/
COPY --from=unpacker /layers/lib3/ /app/BOOT-INF/lib/
COPY --from=unpacker /layers/lib4/ /app/BOOT-INF/lib/
COPY --from=unpacker /unpacked/BOOT-INF/classpath.idx /app/BOOT-INF/classpath.idx
COPY --from=unpacker /unpacked/BOOT-INF/layers.idx /app/BOOT-INF/layers.idx
COPY --from=unpacker /unpacked/BOOT-INF/classes /app/BOOT-INF/classes
EXPOSE 8080
ENV APP_VERSION=1.0.0
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
