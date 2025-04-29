# ------------ STAGE 1: Build Application ------------
FROM gradle:8.1-jdk17 AS builder

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and project metadata first for caching
COPY ../../settings.gradle .
COPY ../../build.gradle .

# Pre-cache Gradle dependencies
RUN gradle --no-daemon dependencies || true

# Copy the full project last (to avoid invalidating cache too early)
COPY ../../ .

# Only build the required module (skip tests)
RUN gradle :services:restaurant-service:bootJar -x test --no-daemon


# ------------ STAGE 2: Runtime Environment ------------
FROM openjdk:17-jdk-slim AS runtime

# Install curl and clean up cache in one layer for smaller image
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Download OpenTelemetry Java Agent
RUN curl -L -o /opentelemetry-javaagent.jar \
    https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.14.0/opentelemetry-javaagent.jar

# Create non-root user
RUN useradd -m appuser

# Set working directory
WORKDIR /app

# Copy built JAR from builder stage
COPY --from=builder /app/services/restaurant-service/build/libs/*.jar app.jar

# Expose port
EXPOSE 8081

# Switch to non-root user
USER appuser

# Define trace collector endpoint
ENV TRACE_COLLECTOR_URL=http://tempo:4318

# Start the app with OpenTelemetry agent
ENTRYPOINT ["java", "-javaagent:/opentelemetry-javaagent.jar", \
  "-Dotel.service.name=restaurant-service", \
  "-Dotel.traces.exporter=otlp", \
  "-Dotel.metrics.exporter=none", \
  "-Dotel.logs.exporter=none", \
  "-Dotel.exporter.otlp.endpoint=http://tempo:4318", \
  "-Dotel.exporter.otlp.protocol=http/protobuf", \
  "-jar", "app.jar"]

