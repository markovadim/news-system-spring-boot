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
import ru.clevertec.newssystemmanagement.dto.CommentDto;
import ru.clevertec.newssystemmanagement.entities.Comment;
import ru.clevertec.newssystemmanagement.exceptions.CommentNotFoundException;
import ru.clevertec.newssystemmanagement.mapping.CommentMapper;
import ru.clevertec.newssystemmanagement.repositories.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentMapper commentMapper;
    @Mock
    private BaseSystemCache<Long, Comment> cache;
    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("Find All")
    void checkFindAllShouldReturnSize3() {
        List<Comment> comments = MockUtil.getComments();
        doReturn(new PageImpl<>(comments)).when(commentRepository).findAll(Pageable.unpaged());
        doReturn(MockUtil.getDtoComments()).when(commentMapper).toDtoList(comments);

        int expectedSize = 3;
        int actualSize = commentService.findAll(Pageable.unpaged()).size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    @DisplayName("Find By Id")
    void checkFindByIdShouldReturnTestName() {
        Comment comment = new Comment(LocalDateTime.now(), "Test Text", "Test Name");
        CommentDto commentDto = CommentDto.builder().text("test dto text").username("test dto name").build();

        doReturn(commentDto).when(commentMapper).toDto(comment);
        doReturn(true).when(cache).containsKey(1L);
        doReturn(comment).when(cache).get(1L);

        String expectedName = commentDto.getUsername();
        String actualName = commentService.findById(1L).getUsername();

        assertEquals(expectedName, actualName);
    }

    @Test
    @DisplayName("Find By Id From DB")
    void checkFindByIdShouldReturn() {
        Comment comment = MockUtil.getComments().get(0);
        CommentDto commentDto = MockUtil.getDtoComments().get(0);

        doReturn(false).when(cache).containsKey(1L);
        doReturn(Optional.of(comment)).when(commentRepository).findById(1L);
        doNothing().when(cache).put(1L, comment);
        doReturn(commentDto).when(commentMapper).toDto(comment);

        String expectedName = commentDto.getUsername();
        String actualName = commentService.findById(1L).getUsername();

        assertEquals(expectedName, actualName);
    }

    @Test
    @DisplayName("Find Comments By News")
    void checkFindCommentsByNewsIdShouldReturnSize3() {
        List<Comment> comments = MockUtil.getComments();
        List<CommentDto> commentDtos = MockUtil.getDtoComments();

        doReturn(commentDtos).when(commentMapper).toDtoList(MockUtil.getComments());
        doReturn(comments).when(commentRepository).findAllByNewsId(1L, Pageable.unpaged());

        int expectedSize = commentDtos.size();
        int actualSize = commentService.findCommentsByNewsId(1L, Pageable.unpaged()).size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    @DisplayName("Save")
    void checkSaveShouldCallSaveMethod() {
        Comment expectedComment = new Comment();
        CommentDto commentDto = CommentDto.builder().build();

        doReturn(expectedComment).when(commentRepository).save(expectedComment);
        doReturn(expectedComment).when(commentMapper).toEntity(commentDto);

        commentService.save(commentDto);

        verify(commentRepository).save(expectedComment);
    }

    @Test
    @DisplayName("Delete with exception")
    void checkRemoveByIdShouldThrowCommentNotFoundException() {
        doThrow(CommentNotFoundException.class).when(commentRepository).findById(1L);

        assertThrows(CommentNotFoundException.class, () -> commentService.removeById(1L));
    }

    @Test
    @DisplayName("Delete")
    void checkRemoveByIdShouldCallMethod() {
        doReturn(true).when(cache).containsKey(1L);
        doReturn(MockUtil.getComments().get(0)).when(cache).get(1L);
        doNothing().when(commentRepository).deleteById(1L);

        commentService.removeById(1L);

        verify(cache).removeByKey(1L);
    }

    @Test
    @DisplayName("Update")
    void checkUpdateByIdShouldCallSaveMethod() {
        Comment comment = new Comment();
        CommentDto commentDto = CommentDto.builder().text("Unknown text").username("New Author").build();

        doReturn(true).when(cache).containsKey(1L);
        doReturn(commentDto).when(commentMapper).toDto(comment);
        doReturn(comment).when(cache).get(1L);
        doReturn(comment).when(commentMapper).toEntity(commentDto);

        commentService.updateById(1L, commentDto);

        verify(commentRepository).save(comment);
    }

    @Test
    @DisplayName("Find With Filter")
    void checkFindCommentsWithFilterByTextOrUsernameShouldReturnSize3() {
        List<Comment> comments = MockUtil.getComments();
        List<CommentDto> commentsDtos = MockUtil.getDtoComments();

        doReturn(comments).when(commentRepository).findAllByTextContainsIgnoreCaseOrUsernameContainsIgnoreCase("Text", "Username");
        doReturn(commentsDtos).when(commentMapper).toDtoList(comments);

        int expectedSize = commentsDtos.size();
        int actualSize = commentService.findCommentsWithFilterByTextOrUsername("Text", "Username").size();

        assertEquals(expectedSize, actualSize);
    }
}