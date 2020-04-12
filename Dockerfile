FROM openjdk:8-jdk-alpine

VOLUME /tmp

EXPOSE 3000
EXPOSE 3001
EXPOSE 3002

RUN mkdir -p /app/
RUN mkdir -p /app/logs/
ADD target/versatile-development.jar /app/versatile-development.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=container", "-jar", "app/versatile-development.jar"]