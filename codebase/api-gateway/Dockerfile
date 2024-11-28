FROM openjdk:17
#FROM public.ecr.aws/docker/library/openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} api-gateway.jar

ENTRYPOINT ["java", "-jar", "api-gateway.jar"]

EXPOSE 4953