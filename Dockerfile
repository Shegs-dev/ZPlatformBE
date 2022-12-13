# For Java 8, try this
# FROM openjdk:8-jdk-alpine

# For Java 11, try this
FROM adoptopenjdk/openjdk11:alpine-jre

VOLUME /tmp

ADD target/zplatform-0.0.1-SNAPSHOT.jar zplatform.jar

EXPOSE 7002

ENTRYPOINT ["java","-jar","/zplatform.jar"]