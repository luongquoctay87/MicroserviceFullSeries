FROM openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} notification-service.jar

ENTRYPOINT ["java", "-jar", "notification-service.jar"]

EXPOSE 8088