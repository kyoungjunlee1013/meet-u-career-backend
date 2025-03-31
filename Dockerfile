FROM gradle:7.2.0-jdk as builder

WORKDIR /app
COPY . .
RUN chmod +x gradlew && ./gradlew clean build --no-daemon

FROM openjdk:17
WORKDIR /app
ARG JAR_FILE=./build/libs/meet-u-backend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /meet-u-backend.jar

ENTRYPOINT [ "java", "-jar", "/meet-u-backend.jar" ]