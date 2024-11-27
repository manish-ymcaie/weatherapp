FROM openjdk:17-jdk-slim
COPY target/weatherapp-0.0.1-SNAPSHOT.jar weather-app.jar
ENTRYPOINT ["java", "-jar", "weather-app.jar"]
