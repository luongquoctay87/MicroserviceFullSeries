FROM openjdk:17
#FROM public.ecr.aws/docker/library/openjdk:17

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} batch-job.jar

ENTRYPOINT ["java", "-jar", "batch-job.jar"]

EXPOSE 8089