## Versatile Development
#### About
This project is developed for training and should not be used for commercial purposes!
<br>You can see this project <a href=http://versatile-development.xyz/>here.</a>

##### Ways to use
* Install docker, run 'sudo docker-compose up --build',
and change some data as below in application.properties,
then just start an application.
* Install PostgreSQL & Redis and then create database 'versatile' in PostgreSQL.

##### Important information
* Now this site uses Redis for saving user Session and also user locale.
* Now this site can work with databases which is in docker, but there are some problems with taking whole project in a container.
* csrf token is disabled temporary.

You can see my task progress in a table: 
https://trello.com/b/j08mhCbw/versatile-development

##### In application.properties you can also use: 
* for postgres in docker: spring.datasource.url=jdbc:postgresql://localhost:3001/versatile
* for redis in docker: spring.redis.port=3002
* for writing logs in custom file: logging.file=your_file_path

##### Technology stack:
* HTML, CSS, JS/JQuery, Thymeleaf;
* Java 8, Hibernate, Spring(Boot, MVC, Data, Security);
* JUnit, Mockito;
* PostgreSQL, Redis.

Created by `CommoMegaSator`.