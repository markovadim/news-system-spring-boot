package ru.clevertec.newssystemmanagement.mapping;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.clevertec.newssystemmanagement.MockUtil;
import ru.clevertec.newssystemmanagement.dto.CommentDto;
import ru.clevertec.newssystemmanagement.entities.Comment;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@SpringBootTest
class CommentMapperTest {

    @Autowired
    private CommentMapper mapper;

    @Test
    void checkToDtoListShouldReturnNull() {
        assertNull(mapper.toDtoList(null));
    }

    @Test
    void checkToDtoListShouldExecuteWithoutExceptions() {
        assertDoesNotThrow(() -> mapper.toDtoList(MockUtil.getComments()));
    }

    @Test
    void checkToDtoShouldReturnNull() {
        assertNull(mapper.toDto(null));
    }

    @Test
    void checkToDtoShouldReturnDtoByEntity() {
        Comment comment = MockUtil.getComments().get(1);
        CommentDto dto = mapper.toDto(comment);

        assertAll(
                () -> assertEquals(comment.getText(), dto.getText()),
                () -> assertEquals(comment.getTime(), dto.getTime()),
                () -> assertEquals(comment.getUsername(), dto.getUsername()),
                () -> assertEquals(comment.getNewsId(), dto.getNewsId())
        );
    }

    @Test
    void checkToEntityShouldReturnEntityByDto() {
        CommentDto dto = CommentDto.builder().text("BuilderText").build();
        Comment comment = mapper.toEntity(dto);

        assertAll(
                () -> assertEquals(dto.getText(), comment.getText()),
                () -> assertNull(comment.getTime()),
                () -> assertNull(comment.getUsername())
        );
    }

    @Test
    void checkToEntityShouldReturnNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void checkUpdateCommentByDtoShouldUpdateOnlyOneField() {
        CommentDto dto = CommentDto.builder()
                .username("Author")
                .text("Text")
                .build();
        Comment comment = new Comment();

        mapper.updateCommentByDto(dto, comment);

        assertAll(
                () -> assertEquals(dto.getUsername(), comment.getUsername()),
                () -> assertNull(comment.getTime()),
                () -> assertEquals(dto.getTime(), comment.getTime()),
                () -> assertEquals(dto.getText(), comment.getText())
        );
    }
}