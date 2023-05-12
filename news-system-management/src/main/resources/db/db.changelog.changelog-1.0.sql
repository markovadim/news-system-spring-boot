--liquibase formatted sql

--changeset liquibase-service:create tables news and comments
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