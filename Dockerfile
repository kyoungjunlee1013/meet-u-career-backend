FROM gradle:7.2.0-jdk as builder

WORKDIR /app
COPY . .
RUN chmod +x gradlew && ./gradlew clean build -x test --no-daemon

FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar /meet-u-backend.jar
ENTRYPOINT ["java", "-jar", "/meet-u-backend.jar"]