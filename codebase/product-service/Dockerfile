FROM openjdk:17
#FROM public.ecr.aws/docker/library/openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} product-service.jar

ENTRYPOINT ["java", "-jar", "product-service.jar"]

EXPOSE 8083