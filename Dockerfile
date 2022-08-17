FROM maven:3.8.3-openjdk-17
MAINTAINER CommoMegaSator
VOLUME /tmp
EXPOSE 3000
RUN mkdir -p /app/
RUN mkdir -p /app/logs/

#ADD target/versatile-development.jar /app/versatile-development.jar
ADD . /
RUN mvn clean install
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "/target/versatile-development.jar"]