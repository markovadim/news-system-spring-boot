package ru.clevertec.newssystemmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.newssystemmanagement.dto.NewsDto;
import ru.clevertec.newssystemmanagement.entities.News;
import ru.clevertec.newssystemmanagement.exceptions.NewsNotFoundException;
import ru.clevertec.newssystemmanagement.mapping.NewsMapper;
import ru.clevertec.newssystemmanagement.repositories.NewsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;


    @Cacheable("news")
    public List<NewsDto> findAll(Pageable pageable) {
        return newsMapper.toDtoList(newsRepository.findAll(pageable).getContent());
    }

    @Cacheable(value = "news", key = "#id")
    public NewsDto findById(long id) {
        News news = newsRepository.findById(id).orElseThrow(() -> new NewsNotFoundException(id));
        return newsMapper.toDto(news);
    }

    @Cacheable(value = "news", key = "#title")
    public NewsDto save(NewsDto newsDto) {
        newsDto.setTime(LocalDateTime.now());
        newsRepository.save(newsMapper.toEntity(newsDto));
        return newsDto;
    }

    @CacheEvict(value = "news", key = "#id", allEntries = true)
    public void removeById(long id) {
        findById(id);
        newsRepository.deleteById(id);
    }

    @CachePut(value = "news", key = "#id")
    public void updateById(long id, NewsDto newsDto) {
        News currentNews = newsMapper.toEntity(findById(id));
        newsMapper.updateNewsByDto(newsDto, currentNews);
        currentNews.setTime(LocalDateTime.now());
        newsRepository.save(currentNews);
    }

    @Caching(cacheable = {
            @Cacheable(value = "news", key = "#title"),
            @Cacheable(value = "news", key = "#text"),
    })
    public List<NewsDto> findAllWithFilterByTitleOrText(String title, String text) {
        return newsMapper.toDtoList(newsRepository.findAllByTitleContainsIgnoreCaseOrTextContainsIgnoreCase(title, text));
    }
}
