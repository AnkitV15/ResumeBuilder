# Stage 1: Build the application
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the Maven wrapper and the pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Copy the source code
COPY src src

# Build the application, skipping tests
RUN ./mvnw package -DskipTests

# Stage 2: Create the final, smaller image
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/resumebuilderapi-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Command to run the application
# The PORT environment variable is automatically set by Render.
# Spring Boot will automatically use it.
CMD ["java", "-jar", "app.jar"]
