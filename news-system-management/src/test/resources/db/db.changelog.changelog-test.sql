--liquibase formatted sql
--changeset test:create-insert test data in db

create table if not exists news
(
    id    bigserial,
    text  varchar(255),
    time  timestamp(6),
    title varchar(255),
    primary key (id)
);

create table if not exists comments
(
    id       bigserial,
    text     varchar(255),
    time     timestamp(6),
    username varchar(255),
    news_id  bigint not null,
    primary key (id),
    foreign key (news_id) references news (id)
);

insert into news (text, time, title)
values ('Short text from news.', now(), 'Title_1'),
       ('Two part of text from news', now(), 'Unknown title'),
       ('Hello world from IDEA', now(), 'Intro'),
       ('Hello world!', now(), 'Intro_2');

insert into comments (text, time, username, news_id)
values ('comment', now(), 'user', 2),
       ('hsucb', now(), 'useruser', 1),
       ('comment_3', now(), 'mark', 2);