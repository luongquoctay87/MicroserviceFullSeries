FROM openjdk:17

ARG JAR_FILE=target/*.jar

ADD ${JAR_FILE} account-service.jar

ENTRYPOINT ["java", "-jar", "account-service.jar"]

EXPOSE 8081