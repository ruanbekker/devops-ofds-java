# ------------ STAGE 1: Build Application ------------
FROM gradle:8.1-jdk17 AS builder

# Set working directory
WORKDIR /app

# Copy Gradle wrapper and config files first to optimize cache
COPY ../settings.gradle .
COPY ../build.gradle .

# Pre-cache Gradle dependencies
RUN gradle --no-daemon dependencies || true

# Copy the rest of the project
COPY ../ .

# Build only the gateway module
RUN gradle :gateway:bootJar -x test --no-daemon


# ------------ STAGE 2: Runtime Environment ------------
FROM openjdk:17-jdk-slim AS runtime

# Install curl and clean up in the same layer to reduce image size
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Download OpenTelemetry Java Agent
RUN curl -L -o /opentelemetry-javaagent.jar \
    https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.14.0/opentelemetry-javaagent.jar

# Create a non-root user for security best practices
RUN useradd -m appuser

# Set working directory
WORKDIR /app

# Copy the JAR built in the previous stage
COPY --from=builder /app/gateway/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Switch to non-root user
USER appuser

# Optional: Define collector URL as env variable
ENV TRACE_COLLECTOR_URL=http://tempo:4318

# Start the app with OpenTelemetry agent
ENTRYPOINT ["java", "-javaagent:/opentelemetry-javaagent.jar", \
  "-Dotel.service.name=api-gateway", \
  "-Dotel.traces.exporter=otlp", \
  "-Dotel.metrics.exporter=none", \
  "-Dotel.logs.exporter=none", \
  "-Dotel.exporter.otlp.endpoint=http://tempo:4318", \
  "-Dotel.exporter.otlp.protocol=http/protobuf", \
  "-jar", "app.jar"]

