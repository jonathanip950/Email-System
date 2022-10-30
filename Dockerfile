FROM adoptopenjdk/openjdk11:jdk-11.0.10_9-centos
ARG JAR_FILE=api/target/email-*.jar
COPY ${JAR_FILE} email-service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/email-service.jar"]
