FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the pre-built JAR file
COPY qa-training-app.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD wget --quiet --tries=1 --spider http://localhost:8080/swagger-ui.html || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
