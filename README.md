# News System
###Spring Boot application for manage news system.
______
###Technology: 
- [x] Spring boot 3.0.6
- [x] Spring Data Jpa
- [x] Spring Web
- [x] PostgreSQL
- [x] Lombok
- [x] Mapstruct
- [x] Junit 5
- [x] Java 17
- [x] Gradle
- [x] Docker compose
- [x] Redis
______

## News

|Method| Endpoint | Description |
|----| ------ | ------ |
|GET|/api/v1/news | Show all news from database |
|GET| /api/v1/news/{id} | Search news by id |
|GET | /api/v1/news/{id}/comments | Show all comments of news by id |
|POST| /api/v1/news | Create news|
|PATCH| /api/v1/news/{id} |Update news by id |
|DELETE| /api/v1/news/{id} | Delete news by id |
|GET| /api/v1/news/filter?title={title}&text={text}| Search news by request param title or text|

## Comments

|Method| Endpoint | Description |
|----| ------ | ------ |
|GET|/api/v1/comments| Show all comments from database |
|GET| /api/v1/comments/{id} | Search comment by id |
|POST| /api/v1/comments | Create comment|
|PATCH| /api/v1/comments/{id} |Update comment by id (and use news_id) |
|DELETE| /api/v1/comments/{id} | Delete comment by id |
|GET| /api/v1/comments/filter?text={text}&username={username} | Search comments by filter (text or username) |

_____
#### ***Used custom implementations of LRU\LFU cache algorithm. Choose algorithm of cache: in property file:***
    cache: LRU (or LFU)
