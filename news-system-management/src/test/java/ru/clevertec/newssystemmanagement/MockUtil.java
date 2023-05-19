package ru.clevertec.newssystemmanagement;

import ru.clevertec.newssystemmanagement.dto.CommentDto;
import ru.clevertec.newssystemmanagement.dto.NewsDto;
import ru.clevertec.newssystemmanagement.entities.Comment;
import ru.clevertec.newssystemmanagement.entities.News;

import java.util.List;

public class MockUtil {

    private MockUtil() {
    }

    public static List<Comment> getComments() {
        /*
         * LocalDateTime param changed on null because test not complete.
         */
        return List.of(
                new Comment(null, "Text_1", "Username_Smirnov"),
                new Comment(null, "Text_2", "Fedorov"),
                new Comment(null, "Text_3", "Ivanov")
        );
    }

    public static List<CommentDto> getDtoComments() {
        return List.of(
                CommentDto.builder().text("Dto text").username("user 1").build(),
                CommentDto.builder().text("Dto text 2v").username("user 2").build(),
                CommentDto.builder().text("Dto text 3v").username("user 1").build()
        );
    }

    public static List<News> getNews() {
        /*
         * LocalDateTime param changed on null because test not complete.
         */
        return List.of(
                new News(null, "Title 1", "Text from test"),
                new News(null, "Title 2", "Hello world"),
                new News(null, "Test title", "I'm groot")
        );
    }

    public static List<NewsDto> getDtoNews(){
        return List.of(
                NewsDto.builder().title("Util class").text("Text builder").build(),
                NewsDto.builder().title("MockUtil class").text("This is utility class for tests").build(),
                NewsDto.builder().title("I'm used mocks").text("Text builder (part 2)").build()
        );
    }
}
