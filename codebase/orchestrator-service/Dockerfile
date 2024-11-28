FROM openjdk:17
#FROM public.ecr.aws/docker/library/openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} orchestrator-service.jar

ENTRYPOINT ["java", "-jar", "orchestrator-service.jar"]

EXPOSE 8090