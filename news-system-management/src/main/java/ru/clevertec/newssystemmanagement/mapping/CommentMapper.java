package ru.clevertec.newssystemmanagement.mapping;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.clevertec.newssystemmanagement.dto.CommentDto;
import ru.clevertec.newssystemmanagement.entities.Comment;

import java.util.List;

/**
 * Mapping Comment entity <-> Comment Dto
 */
@Mapper
public interface CommentMapper {

    /**
     * @param commentList - comment list which need convert to dto list
     * @return dto list
     */
    List<CommentDto> toDtoList(List<Comment> commentList);

    /**
     * @param comment - one comment from database which need convert to dto
     * @return dto comment
     */
    CommentDto toDto(Comment comment);

    /**
     * @param dto - dto comment which need save in database
     * @return comment entity
     */
    Comment toEntity(CommentDto dto);

    /**
     * Update entity by dto comment. Nullable fields - ignore.
     *
     * @param dto    - dto comment with new fields
     * @param entity - comment which need update in database
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCommentByDto(CommentDto dto, @MappingTarget Comment entity);
}
