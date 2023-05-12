--liquibase formatted sql
--changeset liquibase-service:insert data

insert into news (text, time, title)
values ('Short text from news.', now(), 'Title_1'),
       ('Two part of text from news', now(), 'Unknown title'),
       ('Hello world from IDEA', now(), 'Intro'),
       ('Hello world!', now(), 'Intro_2'),
       ('Happy Birthday', now(), 'DR'),
       ('To be or not to be', now(), 'William'),
       ('Hero from city', now(), 'Time'),
       ('Hello world', now(), 'Without title'),
       ('Hello', now(), 'Clevertec'),
       ('Hello Java', now(), 'Internship'),
       ('For testing', now(), 'Test'),
       ('Spring boot for development', now(), 'Spring'),
       ('Spring String Swing', now(), '???'),
       ('Big World', now(), 'Test_2'),
       ('Happy children', now(), 'Family'),
       ('Happy family', now(), 'Family_2'),
       ('No News.......', now(), 'Fake'),
       ('Into IDEA', now(), 'Inside'),
       ('From IDEA', now(), 'Title_3'),
       ('Hello WORLD!!!!', now(), 'Inspect');

insert into comments (text, time, username, news_id)
values ('comment', now(), 'user', 2),
       ('hsucb', now(), 'useruser', 1),
       ('kb3', now(), 'mark', 2),
       ('f4rfffa', now(), 'maxim', 3),
       ('ncakno3', '11-11-2013', 'kira', 4),
       ('v kf32fn', now(), 'ulrih', 4),
       ('hsucb', now(), 'userio', 3),
       ('cnejwc32bca', '08-01-2021', 'useruser', 11),
       ('comment', now(), 'kirill', 18),
       ('comment two part', now(), 'useruser', 5);