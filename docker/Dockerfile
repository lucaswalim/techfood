# 1. Build
FROM maven:3.9.13-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B


COPY src ./src


RUN mvn clean package -DskipTests

# 2. Runtime
FROM eclipse-temurin:21-jdk-alpine AS runtime
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]