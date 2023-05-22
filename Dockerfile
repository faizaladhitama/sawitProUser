FROM openjdk:8-jdk-alpine
MAINTAINER faizaladhitamaprabowo
COPY build/libs/sawitPro-0.0.1-SNAPSHOT.jar sawitPro-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/sawitPro-0.0.1-SNAPSHOT.jar"]