# 1단계: Gradle로 빌드
FROM gradle:8.5-jdk17 AS build
WORKDIR /home/gradle/src

# Gradle 설정과 소스 복사
COPY --chown=gradle:gradle build.gradle settings.gradle gradle/ ./
COPY --chown=gradle:gradle src ./src

# 빌드
RUN gradle clean build --no-daemon

# 2단계: 실행 이미지
FROM eclipse-temurin:17-jdk
WORKDIR /app

# JAR 복사
COPY --from=build /home/gradle/src/build/libs/*.jar /app/meet-u-backend.jar

# 포트
EXPOSE 8080

# 실행
ENTRYPOINT ["java", "-jar", "/app/meet-u-backend.jar"]
