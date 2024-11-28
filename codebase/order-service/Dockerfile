FROM openjdk:17
#FROM public.ecr.aws/docker/library/openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} order-service.jar

ENTRYPOINT ["java", "-jar", "order-service.jar"]

EXPOSE 8084