package ru.clevertec.newssystemmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.newssystemmanagement.entities.News;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {

    /**
     * Search news by filter.
     *
     * @param title - title of news (can to use part of keyword).
     * @param text  - text or part of text.
     * @return - news list
     */
    List<News> findAllByTitleContainsIgnoreCaseOrTextContainsIgnoreCase(String title, String text);
}
