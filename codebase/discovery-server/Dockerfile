FROM openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} /opt/discovery-server.jar

ENTRYPOINT ["java","-jar","/opt/discovery-server.jar"]

EXPOSE 8761