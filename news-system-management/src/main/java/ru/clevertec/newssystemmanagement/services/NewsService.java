package ru.clevertec.newssystemmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.newssystemmanagement.cache.BaseSystemCache;
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
    private final BaseSystemCache<Long, News> cache;


    public List<NewsDto> findAll(Pageable pageable) {
        return newsMapper.toDtoList(newsRepository.findAll(pageable).getContent());
    }

    public NewsDto findById(long id) {
        if (cache.containsKey(id)) {
            return newsMapper.toDto(cache.get(id));
        } else {
            News news = newsRepository.findById(id).orElseThrow(() -> new NewsNotFoundException(id));
            cache.put(id, news);
            return newsMapper.toDto(news);
        }
    }

    public void save(NewsDto newsDto) {
        newsDto.setTime(LocalDateTime.now());
        News savedNews = newsRepository.save(newsMapper.toEntity(newsDto));
        cache.put(savedNews.getId(), savedNews);
    }

    public void removeById(long id) {
        findById(id);
        newsRepository.deleteById(id);
        cache.removeByKey(id);
    }

    public void updateById(long id, NewsDto newsDto) {
        News currentNews = newsMapper.toEntity(findById(id));
        newsMapper.updateNewsByDto(newsDto, currentNews);
        currentNews.setTime(LocalDateTime.now());
        newsRepository.save(currentNews);
        cache.put(id, currentNews);
    }

    public List<NewsDto> findAllWithFilterByTitleOrText(String title, String text) {
        return newsMapper.toDtoList(newsRepository.findAllByTitleContainsIgnoreCaseOrTextContainsIgnoreCase(title, text));
    }
}
