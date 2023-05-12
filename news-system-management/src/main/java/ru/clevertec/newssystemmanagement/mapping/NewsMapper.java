package ru.clevertec.newssystemmanagement.mapping;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.clevertec.newssystemmanagement.dto.NewsDto;
import ru.clevertec.newssystemmanagement.entities.News;

import java.util.List;

/**
 * Mapping News entity <-> News Dto
 */
@Mapper
public interface NewsMapper {

    /**
     * @param newsList - news list which need convert to dto list
     * @return dto list
     */
    List<NewsDto> toDtoList(List<News> newsList);

    /**
     * @param news - one news from database which need convert to dto
     * @return dto news
     */
    NewsDto toDto(News news);

    /**
     * @param newsDto - dto news which need save in database
     * @return news entity
     */
    News toEntity(NewsDto newsDto);

    /**
     * Update entity by dto news. Nullable fields - ignore.
     *
     * @param dto    - dto news with new fields
     * @param entity - news which need update in database
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateNewsByDto(NewsDto dto, @MappingTarget News entity);
}
