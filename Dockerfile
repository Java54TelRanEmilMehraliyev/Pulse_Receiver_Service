FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/pulse-receiver-service-0.0.1-SNAPSHOT.jar pulse-receiver-service.jar
EXPOSE 5005
CMD ["java", "-jar", "pulse-receiver-service.jar"]