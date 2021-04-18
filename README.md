## Versatile Development
#### About
This project is develop for training and should not be used for commercial purposes!
<br>You can see this project <a href=http://versatile-development.xyz/>here.</a>

##### Software that you need
* Java 11.
* Maven.
* PostgreSQL.
* Redis.

##### Ways to use
* Create database 'versatile' in PostgreSQL.
* Compile via `mvn clean install` command.
* Start via - `java -jar target/versatile-development.jar` command.

##### Important information
* For using locally change profile to dev:\
&emsp;Go to Edit Configuration and change program arguments to \
&emsp;`--spring.profiles.active=dev`.
* Redis saves user Session and user locale.

You can see my task progress in a table: 
https://trello.com/b/j08mhCbw/versatile-development

##### Technology stack:
* HTML, CSS, Bootstrap, JS/JQuery, Thymeleaf;
* Java 11, Hibernate, Spring(Boot, MVC, Data, Security);
* JUnit 5, Mockito;
* PostgreSQL, Redis, Flyway.

##### Installing Java on Linux
* Download jdk-11.
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

##### Testing application for any user
* Login: `Default`
* Password: `DefaultChecker`
    
Created by `CommoMegaSator`.