# Stage 1: Build the application
FROM gradle:8.1-jdk17 AS builder

# Set the working directory
WORKDIR /app

# Copy the root project files
COPY ../../settings.gradle ../../build.gradle ./

# Copy the entire project
COPY ../../ .

# Build the application using Gradle
RUN gradle :services:delivery-service:clean :services:delivery-service:bootJar -x test

# Stage 2: Create a lightweight image to run the application
FROM openjdk:17-jdk-slim

# Download OpenTelemetry Java Agent
RUN apt update; apt install curl -y
RUN curl -L -o /opentelemetry-javaagent.jar \
    https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.14.0/opentelemetry-javaagent.jar

# Create a user to run the application
RUN useradd -m appuser

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=builder /app/services/delivery-service/build/libs/*.jar app.jar

# Expose the application port
EXPOSE 8083

# Run the application as the created user
USER appuser

# Trace Collector # http://jaeger:4318 or http://tempo:4318
ENV TRACE_COLLECTOR_URL=http://tempo:4318

# Start the application with opentelemetry
ENTRYPOINT ["java", "-javaagent:/opentelemetry-javaagent.jar", \
  "-Dotel.service.name=delivery-service", \
  "-Dotel.traces.exporter=otlp", \
  "-Dotel.metrics.exporter=none", \
  "-Dotel.logs.exporter=none", \
  "-Dotel.exporter.otlp.endpoint=http://tempo:4318", \
  "-Dotel.exporter.otlp.protocol=http/protobuf", \
  "-jar", "app.jar"]
