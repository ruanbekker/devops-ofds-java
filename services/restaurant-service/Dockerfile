# Stage 1: Build the application
FROM gradle:8.1-jdk17 AS builder

# Set the working directory
WORKDIR /app

# Copy the root project files
COPY ../../settings.gradle ../../build.gradle ./

# Copy the entire project
COPY ../../ .

# Build the application using Gradle
RUN gradle :services:restaurant-service:clean :services:restaurant-service:bootJar -x test

# Stage 2: Create a lightweight image to run the application
FROM openjdk:17-jdk-slim

# Create a user to run the application
RUN useradd -m appuser

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=builder /app/services/restaurant-service/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8082

# Run the application as the created user
USER appuser

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]

