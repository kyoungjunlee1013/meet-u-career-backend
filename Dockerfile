# 1단계: Gradle로 빌드
FROM gradle:8.5-jdk17 AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
RUN gradle clean build -x test --no-daemon   # ← 테스트 스킵

# 2단계: 실행 이미지
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/meet-u-backend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/meet-u-backend.jar"]
