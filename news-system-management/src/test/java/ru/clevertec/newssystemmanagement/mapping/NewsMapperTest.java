package ru.clevertec.newssystemmanagement.mapping;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.clevertec.newssystemmanagement.MockUtil;
import ru.clevertec.newssystemmanagement.dto.NewsDto;
import ru.clevertec.newssystemmanagement.entities.News;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NewsMapperTest {

    @Autowired
    private NewsMapper mapper;

    @Test
    void checkToDtoListShouldReturnNull() {
        assertNull(mapper.toDtoList(null));
    }

    @Test
    void checkToDtoListShouldReturnSize3() {
        List<NewsDto> newsDtos = mapper.toDtoList(MockUtil.getNews());

        int expectedSize = MockUtil.getNews().size();
        int actualSize = newsDtos.size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    void checkToDtoShouldReturnNull() {
        assertNull(mapper.toDto(null));
    }

    @Test
    void checkToDtoShouldReturnExpectedText() {
        News news = new News(LocalDateTime.parse("2007-12-03T10:15:30"), "Title", "Text");
        NewsDto dto = mapper.toDto(news);

        String expectedText = news.getText();
        String actualText = dto.getText();

        assertEquals(expectedText, actualText);
    }

    @Test
    void checkToEntityShouldConvertDtoToEntityWithNullFields() {
        NewsDto dto = NewsDto.builder().title("Only Title").build();
        News news = mapper.toEntity(dto);

        assertNull(news.getText());
    }

    @Test
    void checkToEntityShouldReturnNull() {
        News news = mapper.toEntity(null);

        assertNull(news);
    }

    @Test
    void checkUpdateNewsByDtoShouldUpdateEntity() {
        NewsDto dto = NewsDto.builder()
                .title("Updated title")
                .text("Updated text")
                .time(LocalDateTime.parse("2044-12-03T10:15:30"))
                .comments(MockUtil.getDtoComments())
                .build();
        News news = new News(LocalDateTime.now(), "Title", "Text");

        mapper.updateNewsByDto(dto, news);
        String expectedText = dto.getText();
        String actualText = news.getText();

        String expectedTitle = news.getTitle();
        String actualTitle = news.getTitle();

        assertAll(
                () -> assertEquals(expectedText, actualText),
                () -> assertEquals(expectedTitle, actualTitle),
                () -> assertEquals(news.getTime().getYear(), 2044),
                () -> assertNotNull(news.getComments())
        );
    }
}