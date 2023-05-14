package ru.clevertec.newssystemmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.newssystemmanagement.dto.CommentDto;
import ru.clevertec.newssystemmanagement.entities.Comment;
import ru.clevertec.newssystemmanagement.exceptions.CommentNotFoundException;
import ru.clevertec.newssystemmanagement.mapping.CommentMapper;
import ru.clevertec.newssystemmanagement.repositories.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Cacheable("comments")
    public List<CommentDto> findAll(Pageable pageable) {
        return commentMapper.toDtoList(commentRepository.findAll(pageable).getContent());
    }

    @Cacheable(value = "comment", key = "#id")
    public CommentDto findById(long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(id));
        return commentMapper.toDto(comment);
    }

    @Cacheable(value = "comments", key = "#newsId")
    public List<CommentDto> findCommentsByNewsId(long id, Pageable pageable) {
        return commentMapper.toDtoList(commentRepository.findAllByNewsId(id, pageable));
    }

    @Cacheable(value = "comment", key = "#username")
    public CommentDto save(CommentDto commentDto) {
        commentDto.setTime(LocalDateTime.now());
        commentRepository.save(commentMapper.toEntity(commentDto));
        return commentDto;
    }

    @CacheEvict(value = "comment", key = "#id")
    public void removeById(long id) {
        findById(id);
        commentRepository.deleteById(id);
    }

    @CachePut(value = "comment", key = "#id")
    public void updateById(long id, CommentDto commentDto) {
        Comment currentComment = commentMapper.toEntity(findById(id));
        commentMapper.updateCommentByDto(commentDto, currentComment);
        currentComment.setTime(LocalDateTime.now());
        commentRepository.save(currentComment);
    }

    @Caching(cacheable = {
            @Cacheable(value = "comment", key = "#username"),
            @Cacheable(value = "comment", key = "#text"),
    })
    public List<CommentDto> findCommentsWithFilterByTextOrUsername(String text, String username) {
        return commentMapper.toDtoList(commentRepository.findAllByTextContainsIgnoreCaseOrUsernameContainsIgnoreCase(text, username));
    }
}
