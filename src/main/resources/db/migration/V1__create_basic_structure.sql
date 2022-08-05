CREATE TABLE IF NOT EXISTS usr (
    id                          bigserial not null primary key,
    email                       varchar(25) not null,
    firstname                   varchar(25),
    lastname                    varchar(25),
    nickname                    varchar(25) not null,
    gender                      varchar(15),
    city                        varchar(60),
    nationality                 varchar(60),
    birthday                    timestamp,
    age                         integer,
    about_user                  varchar(1000),
    password                    varchar(60) not null,
    creation_date               timestamp,
    activated                   boolean default false,
    confirmation_token          varchar(255),
    friends_number              integer default 0,
    reports                     integer,
    token_expiration            timestamp
);

CREATE TABLE IF NOT EXISTS article (
    id                          bigserial not null primary key,
    creation_date               timestamp,
    title                       varchar(80) not null,
    text                        text not null,
    user_id         bigint not null
        constraint  fk_user_id
        REFERENCES  usr(id)
        on update cascade on delete cascade
);

CREATE TABLE IF NOT EXISTS user_role (
    user_id         bigint not null
        constraint  fk_user_id
        REFERENCES  usr(id)
        on update cascade on delete cascade,
    roles           varchar(255)
);

insert into usr (email, nickname, password, activated, creation_date) values ('default@versatile.dev', 'Default', '$2a$10$wCBbqNOugUOHmwPLNfXn0e9Hy/glfVc41jjBb/fPLfdG6RnbzlT.a', true, CURRENT_TIMESTAMP);
insert into usr (email, nickname, password, activated, creation_date) values ('admin@versatile.dev', 'Adminus', '$2a$10$sx7o8gUzbEVAo2FY59NQQu3l.Z2cwYrZLCaRGH2lQoX6C6.n2Sif6', true, CURRENT_TIMESTAMP);
insert into user_role values (1, 'USER');
insert into user_role values (2, 'USER');
insert into user_role values (2, 'ADMIN');