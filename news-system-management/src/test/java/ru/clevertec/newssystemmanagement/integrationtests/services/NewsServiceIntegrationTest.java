package ru.clevertec.newssystemmanagement.integrationtests.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import ru.clevertec.newssystemmanagement.cache.BaseSystemCache;
import ru.clevertec.newssystemmanagement.dto.NewsDto;
import ru.clevertec.newssystemmanagement.entities.News;
import ru.clevertec.newssystemmanagement.exceptions.NewsNotFoundException;
import ru.clevertec.newssystemmanagement.integrationtests.BaseIntegrationTest;
import ru.clevertec.newssystemmanagement.services.NewsService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class NewsServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private NewsService newsService;
    @Autowired
    private BaseSystemCache<Long, News> cache;

    @AfterEach
    void cacheCleaning(){
        cache.clear();
    }

    @Test
    void checkFindAllShouldReturnNotEmptyList() {
        assertFalse(newsService.findAll(Pageable.unpaged()).isEmpty());
    }

    @Test
    void checkFindByIdShouldThrowNewsNotFoundException() {
        assertThrows(NewsNotFoundException.class, () -> newsService.findById(555));
    }

    @Test
    void checkFindByIdShouldReturnNews() {
        NewsDto newsDto = newsService.findById(1L);

        String expectedTitle = "Title_1";
        String actualTitle = newsDto.getTitle();

        assertEquals(expectedTitle, actualTitle);
    }

    @Test
    void checkSaveShouldCreateNews() {
        NewsDto newsDto = NewsDto.builder()
                .title("From test")
                .text("This text from testcontainers")
                .build();

        assertDoesNotThrow(() -> newsService.save(newsDto));
    }

    @Test
    void checkRemoveByIdShouldRemoveNewsFromDb() {
        assertDoesNotThrow(() -> newsService.removeById(1L));
    }

    @Test
    void checkRemoveByIdShouldThrowNewsNotFoundException() {
        assertThrows(NewsNotFoundException.class, () -> newsService.removeById(Long.MAX_VALUE));
    }

    @Test
    void checkUpdateByIdShouldChangeTimeField() {
        NewsDto dto = NewsDto.builder()
                .title("New Title")
                .text("Updated Text")
                .build();

        LocalDateTime currentDateTime = newsService.findById(2L).getTime();
        newsService.updateById(2L, dto);
        LocalDateTime updatedDateTime = newsService.findById(2L).getTime();

        assertNotEquals(currentDateTime, updatedDateTime);
    }

    @Test
    void checkFindAllWithFilterByTitleOrTextShouldReturn3News() {
        List<NewsDto> dtoList = newsService.findAllWithFilterByTitleOrText("title", "textnotfound");
dtoList.forEach(System.out::println);
        int expectedSize = 2;
        int actualSize = dtoList.size();

        assertEquals(expectedSize, actualSize);
    }
}
