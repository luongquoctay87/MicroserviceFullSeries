FROM openjdk:17

ARG JAR_FILE=target/*.jar

ADD ${JAR_FILE} api-gateway.jar

ENTRYPOINT ["java", "-jar", "api-gateway.jar"]

EXPOSE 4953