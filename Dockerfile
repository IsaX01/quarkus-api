FROM openjdk:17-slim
EXPOSE 8888
VOLUME /app
COPY target/quarkus-api-1.0.0-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "/app.jar"]
