package ru.clevertec.newssystemmanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.newssystemmanagement.cache.BaseSystemCache;
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
    private final BaseSystemCache<Long, Comment> cache;

    public List<CommentDto> findAll(Pageable pageable) {
        return commentMapper.toDtoList(commentRepository.findAll(pageable).getContent());
    }

    public CommentDto findById(long id) {
        if (cache.containsKey(id)) {
            return commentMapper.toDto(cache.get(id));
        } else {
            Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(id));
            cache.put(id, comment);
            return commentMapper.toDto(comment);
        }
    }

    public List<CommentDto> findCommentsByNewsId(long id, Pageable pageable) {
        return commentMapper.toDtoList(commentRepository.findAllByNewsId(id, pageable));
    }

    public void save(CommentDto commentDto) {
        commentDto.setTime(LocalDateTime.now());
        Comment savedComment = commentRepository.save(commentMapper.toEntity(commentDto));
        cache.put(savedComment.getId(), savedComment);
    }

    public void removeById(long id) {
        findById(id);
        commentRepository.deleteById(id);
        cache.removeByKey(id);
    }

    public void updateById(long id, CommentDto commentDto) {
        Comment currentComment = commentMapper.toEntity(findById(id));
        commentMapper.updateCommentByDto(commentDto, currentComment);
        currentComment.setTime(LocalDateTime.now());
        commentRepository.save(currentComment);
        cache.put(id, currentComment);
    }

    public List<CommentDto> findCommentsWithFilterByTextOrUsername(String text, String username) {
        return commentMapper.toDtoList(commentRepository.findAllByTextContainsIgnoreCaseOrUsernameContainsIgnoreCase(text, username));
    }
}
