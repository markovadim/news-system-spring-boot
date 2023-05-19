create table if not exists users
(
    id       bigserial   not null primary key,
    username varchar(50),
    email    varchar(50) not null unique,
    password    varchar(100) not null,
    role     varchar(50) default 'SUBSCRIBER'
--     constraint foreign key (role) references role (role.id)
);

insert into users (username, email, password, role)
values ('Admin Adminov', 'admin', '$2a$12$6mE.2F62VSibsR3pnHjXpu2oenI3eRsw5PKczKHwNzsCkxQL8vX/O', 'ADMIN'),
        ('User Userov', 'user', '$2a$12$SItg05GAfwLtK/ZVdYWOg.tjcu7452tO7USqM1j5bQlhi6.cmLAeO', 'SUBSCRIBER'),
       ('Journalist Journalistov', 'journalist', '$2a$12$1K5pMBXqywSCgMZwXtm.kOOFB4PL0gG6JWn1VIBZhqpN43WKImdnG', 'JOURNALIST');