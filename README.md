## Versatile Development

[![Versatile license](https://img.shields.io/badge/license-Apache%202.0-blue.svg?style=flat-square)](LICENSE) [![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](CONTRIBUTING.md)

### Useful information
#### About
This project is developed for training and should not be used for commercial purposes!
<br>You can see this project [here](http://versatile-development.xyz) (not available for now).
<br>You can find api details using: http://localhost:8081/swagger-ui.html (Authorization needed)
<br>This application can be started via docker-compose now!

##### Software that you need
* Java 17.
* Maven 3.8.3.
* PostgreSQL 14.
* Redis 5.0.7.

##### Ways to use
* Create database 'versatile' in PostgreSQL.
* Compile via `mvn clean install` command.
* Start via - `java -jar target/versatile-development.jar` command.

##### Important information
* For using locally change profile to dev:\
&emsp;Go to Edit Configuration and change program arguments to \
&emsp;`--spring.profiles.active=dev`.
* Redis saves user Session and user locale.

##### Technology stack:
* HTML, CSS, Bootstrap, JS/JQuery, Thymeleaf;
* Java 17, Hibernate, Spring(Boot, MVC, Data, Security);
* JUnit 5, Mockito;
* PostgreSQL, Redis, Flyway, Swagger 3.

### Installing needed software
##### Installing Java on Linux
* Download [jdk-17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).
* Install maven via `sudo apt install maven`
* Set JAVA_HOME:
   1) `export $JAVA_HOME/=Path into JDK directory`
   2) `export PATH=$PATH:$JAVA_HOME/bin`
   3) `source /etc/environment`

##### Installing PostgreSQL on Linux
* `sudo su -`
* `apt-get install postgresql postgresql-contrib`
* `update-rc.d postgresql enable`
* `service postgresql start`

##### Installing Redis on Linux
* `sudo apt update`
* `sudo apt install redis-server`
* `sudo nano /etc/redis/redis.conf`
* Change supervised to 'systemd'.
* `sudo systemctl restart redis.service`

### Testing application
##### Testing application for any user
* Login: `Default`
* Password: `DefaultChecker111?`

##### Testing application for premium user
* Login: `Pantalony`
* Password: `AstalavistaPantalony38*`

##### Testing application for administrators
* Login: `Adminus`
* Password: `Administrator1!`

Created by `CommoMegaSator`.
