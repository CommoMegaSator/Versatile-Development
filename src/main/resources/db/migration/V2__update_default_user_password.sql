update usr set password = '$2a$10$juJwntMUSXoYFocl1nBI9Oye3OhjgQlWlHApEnkP4TQms.wIQaIHG' where id = 1;
insert into usr (email, nickname, password, activated, creation_date) values ('premier@versatile.dev', 'Pantalony', '$2a$10$XI9OHJ6IycKU1lqn8UzyzunbsnR.ngtw9mcdI.83ShWfoev68.LcS', true, CURRENT_TIMESTAMP);
insert into user_role values (3, 'USER');
insert into user_role values (3, 'PREMIUM');