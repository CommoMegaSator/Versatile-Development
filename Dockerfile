FROM openjdk:17-alpine
MAINTAINER CommoMegaSator
VOLUME /tmp
EXPOSE 3000
RUN mkdir -p /app/
RUN mkdir -p /app/logs/
ADD target/versatile-development.jar /app/versatile-development.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "/app/versatile-development.jar"]