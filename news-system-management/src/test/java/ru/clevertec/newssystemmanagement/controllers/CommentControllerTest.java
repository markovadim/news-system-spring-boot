package ru.clevertec.newssystemmanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.newssystemmanagement.MockUtil;
import ru.clevertec.newssystemmanagement.dto.CommentDto;
import ru.clevertec.newssystemmanagement.exceptions.CommentNotFoundException;
import ru.clevertec.newssystemmanagement.services.CommentService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CommentService commentService;


    @Test
    void checkFindAllCommentsShouldReturnStatusOK() throws Exception {
        doReturn(MockUtil.getDtoComments()).when(commentService).findAll(Pageable.unpaged());

        mockMvc.perform(get("/api/v1/comments"))
                .andExpect(status().isOk());
    }

    @Test
    void checkFindCommentByIdShouldReturnCommentsAndStatusOk() throws Exception {
        doReturn(MockUtil.getDtoComments().get(0)).when(commentService).findById(1L);

        mockMvc.perform(get("/api/v1/comments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void checkFindCommentByIdShouldReturnStatusNotFound() throws Exception {
        doThrow(CommentNotFoundException.class).when(commentService).findById(1L);

        mockMvc.perform(get("/api/v1/comments/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void checkCreateCommentShouldReturnStatusCreated() throws Exception {
        CommentDto commentDto = CommentDto.builder().text("Text builder").username("Builder").build();
        doNothing().when(commentService).save(commentDto);

        mockMvc.perform(post("/api/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void checkUpdateCommentByIdShouldReturnStatusOkAndDto() throws Exception {
        CommentDto commentDto = CommentDto.builder().text("Text builder").username("Builder").build();
        doNothing().when(commentService).updateById(1L, commentDto);

        mockMvc.perform(patch("/api/v1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(commentDto.getText()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void checkDeleteCommentByIdShouldReturnStatusNoContent() throws Exception {
        doNothing().when(commentService).removeById(1L);

        mockMvc.perform(delete("/api/v1/comments/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void checkFindCommentsWithFilterByTitleOrTextShouldReturnStatusOkAndJson() throws Exception {
        doReturn(MockUtil.getDtoComments()).when(commentService).findCommentsWithFilterByTextOrUsername("text", "username");

        mockMvc.perform(get("/api/v1/comments/filter")
                        .param("text", "text")
                        .param("username", "username"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}