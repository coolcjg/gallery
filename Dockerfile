FROM openjdk:17-alpine AS builder
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 9000
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=prod", "/app.jar"]