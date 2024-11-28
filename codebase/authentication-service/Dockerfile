FROM openjdk:17
#FROM public.ecr.aws/docker/library/openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} authentication-service.jar

ENTRYPOINT ["java", "-jar", "authentication-service.jar"]

EXPOSE 8081