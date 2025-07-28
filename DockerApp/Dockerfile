FROM eclipse-temurin:24-jdk

WORKDIR /app

COPY CarRequest-demo-1.0.jar app.jar

COPY payload.json .

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
