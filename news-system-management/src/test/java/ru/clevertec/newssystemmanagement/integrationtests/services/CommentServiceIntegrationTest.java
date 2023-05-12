package ru.clevertec.newssystemmanagement.integrationtests.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import ru.clevertec.newssystemmanagement.cache.BaseSystemCache;
import ru.clevertec.newssystemmanagement.dto.CommentDto;
import ru.clevertec.newssystemmanagement.entities.Comment;
import ru.clevertec.newssystemmanagement.exceptions.CommentNotFoundException;
import ru.clevertec.newssystemmanagement.integrationtests.BaseIntegrationTest;
import ru.clevertec.newssystemmanagement.services.CommentService;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class CommentServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CommentService commentService;
    @Autowired
    private BaseSystemCache<Long, Comment> cache;

    @AfterEach
    void cacheCleaning(){
        cache.clear();
    }

    @Test
    void checkFindAllShouldReturnNotEmptyList() {
        assertFalse(commentService.findAll(Pageable.unpaged()).isEmpty());
    }

    @Test
    void checkFindByIdShouldThrowCommentNotFoundException() {
        assertThrows(CommentNotFoundException.class, () -> commentService.findById(Long.MAX_VALUE));
    }

    @Test
    void checkFindByIdShouldReturnComment() {
        CommentDto commentDto = commentService.findById(3L);
        String expectedComment = "comment_3";
        String actualComment = commentDto.getText();

        assertEquals(expectedComment, actualComment);
    }

    @Test
    void checkFindCommentsByNewsIdShouldReturn2Comments() {
        List<CommentDto> commentDtos = commentService.findCommentsByNewsId(2L, Pageable.unpaged());
        int expectedSize = 2;
        int actualSize = commentDtos.size();

        assertEquals(expectedSize, actualSize);
    }

    @Test
    void checkSaveShouldSaveNewComment() {
        CommentDto dto = CommentDto.builder()
                .text("New Comment From Test Container")
                .username("Docker")
                .newsId(1)
                .build();

        commentService.save(dto);
        List<String> savedUsername = commentService.findAll(Pageable.unpaged())
                .stream()
                .map(CommentDto::getUsername)
                .filter(p -> p.equalsIgnoreCase("docker"))
                .collect(Collectors.toList());

        assertTrue(savedUsername.contains("Docker"));
    }

    @Test
    void checkRemoveByIdShouldRemoveCommentFromDB() {
        assertDoesNotThrow(() -> commentService.removeById(2L));
    }

    @Test
    void checkRemoveByIdShouldThrowCommentNotFoundException() {
        assertThrows(CommentNotFoundException.class, () -> commentService.removeById(Long.MAX_VALUE));
    }

    @Test
    void checkUpdateByIdShouldUpdateCommentDateInDb() {
        CommentDto currentDto = commentService.findById(1L);
        CommentDto newDto = CommentDto.builder()
                .text("Updated text")
                .username("Updated username")
                .newsId(1)
                .build();

        commentService.updateById(1L, newDto);
        CommentDto updatedDto = commentService.findById(1L);

        assertNotEquals(currentDto.getTime(), updatedDto.getTime());
    }

    @Test
    void checkFindCommentsWithFilterShouldReturn2Comments() {
        List<CommentDto> dtoList = commentService.findCommentsWithFilterByTextOrUsername("comment", "user");

        List<String> mapToCommentText = dtoList
                .stream()
                .map(CommentDto::getText)
                .collect(Collectors.toList());

        assertTrue(mapToCommentText.contains("comment"));
    }
}
