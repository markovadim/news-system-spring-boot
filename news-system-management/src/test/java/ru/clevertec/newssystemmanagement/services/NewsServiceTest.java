package ru.clevertec.newssystemmanagement.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.clevertec.newssystemmanagement.MockUtil;
import ru.clevertec.newssystemmanagement.cache.BaseSystemCache;
import ru.clevertec.newssystemmanagement.dto.NewsDto;
import ru.clevertec.newssystemmanagement.entities.News;
import ru.clevertec.newssystemmanagement.exceptions.NewsNotFoundException;
import ru.clevertec.newssystemmanagement.mapping.NewsMapper;
import ru.clevertec.newssystemmanagement.repositories.NewsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsServiceTest {

    @Mock
    private NewsRepository newsRepository;
    @Mock
    private NewsMapper newsMapper;
    @Mock
    private BaseSystemCache<Long, News> cache;
    @InjectMocks
    private NewsService newsService;

    @Test
    @DisplayName("Find All")
    void checkFindAllShouldReturnListSize3() {
        List<News> news = MockUtil.getNews();
        List<NewsDto> newsDtos = MockUtil.getDtoNews();
        doReturn(new PageImpl<>(news)).when(newsRepository).findAll(Pageable.unpaged());
        doReturn(newsDtos).when(newsMapper).toDtoList(news);

        int expectedSize = newsDtos.size();
        int actualSize = newsService.findAll(Pageable.unpaged()).size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    @DisplayName("Find By Id")
    void checkFindByIdShouldCallPutInCacheMethod() {
        News news = new News(LocalDateTime.now(), "Title", "Text");
        doReturn(false).when(cache).containsKey(1L);
        doReturn(Optional.of(news)).when(newsRepository).findById(1L);

        newsService.findById(1L);

        verify(cache).put(1L, news);
    }

    @Test
    @DisplayName("Save")
    void checkSaveShouldCallCachePutMethod() {
        News news = MockUtil.getNews().get(0);
        NewsDto newsDto = NewsDto.builder().title("Builder").text("Text").build();
        doReturn(news).when(newsMapper).toEntity(newsDto);
        doReturn(news).when(newsRepository).save(news);

        newsService.save(newsDto);

        verify(cache).put(news.getId(), news);
    }

    @Test
    @DisplayName("Delete with exception")
    void checkRemoveByIdShouldThrowNewsNotFoundException() {
        doThrow(NewsNotFoundException.class).when(newsRepository).findById(1L);

        assertThrows(NewsNotFoundException.class, () -> newsService.removeById(1L));
    }

    @Test
    @DisplayName("Delete")
    void checkRemoveByIdShouldCallCacheRemoveMethod() {
        News news = MockUtil.getNews().get(0);
        NewsDto newsDto = MockUtil.getDtoNews().get(0);
        doReturn(true).when(cache).containsKey(1L);
        doReturn(news).when(cache).get(1L);
        doReturn(newsDto).when(newsMapper).toDto(news);
        doNothing().when(newsRepository).deleteById(1L);

        newsService.removeById(1L);

        verify(cache).removeByKey(1L);
    }

    @Test
    @DisplayName("Update")
    void checkUpdateByIdShouldReturnNotNullTime() {
        News news = MockUtil.getNews().get(0);
        NewsDto newsDto = NewsDto.builder().title("One").text("First text").build();
        doReturn(false).when(cache).containsKey(1L);
        doReturn(Optional.of(news)).when(newsRepository).findById(1L);
        doReturn(news).when(newsMapper).toEntity(newsDto);
        doReturn(newsDto).when(newsMapper).toDto(news);
        doNothing().when(newsMapper).updateNewsByDto(newsDto, news);
        doReturn(news).when(newsRepository).save(news);
        doNothing().when(cache).put(1L, news);

        newsService.updateById(1L, newsDto);

        assertNotNull(news.getTime());
    }

    @Test
    @DisplayName("Find With Filter")
    void checkFindAllWithFilterByTitleOrTextShouldReturnNotEmptyList() {
        List<News> news = MockUtil.getNews();
        List<NewsDto> newsDtos = MockUtil.getDtoNews();

        doReturn(news).when(newsRepository).findAllByTitleContainsIgnoreCaseOrTextContainsIgnoreCase("test title", "test text");
        doReturn(newsDtos).when(newsMapper).toDtoList(news);

        assertFalse(newsService.findAllWithFilterByTitleOrText("test title", "test text").isEmpty());
    }
}