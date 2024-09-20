FROM openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} product-service.jar

ENTRYPOINT ["java", "-jar", "product-service.jar"]

EXPOSE 8083