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
import ru.clevertec.newssystemmanagement.dto.NewsDto;
import ru.clevertec.newssystemmanagement.exceptions.NewsNotFoundException;
import ru.clevertec.newssystemmanagement.services.CommentService;
import ru.clevertec.newssystemmanagement.services.NewsService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NewsController.class)
class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private NewsService newsService;
    @MockBean
    private CommentService commentService;

    @Test
    void checkFindAllNewsShouldReturnStatusOKAndJsonType() throws Exception {
        doReturn(MockUtil.getDtoNews()).when(newsService).findAll(Pageable.unpaged());

        mockMvc.perform(get("/api/v1/news"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void checkFindNewsByIdShouldReturnStatusNotFound() throws Exception {
        doThrow(NewsNotFoundException.class).when(newsService).findById(1L);

        mockMvc.perform(get("/api/v1/news/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void checkFindNewsByIdShouldReturnNewsInJsonFormat() throws Exception {
        doReturn(MockUtil.getDtoNews().get(0)).when(newsService).findById(1L);

        mockMvc.perform(get("/api/v1/news/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.text").value("Text builder"));
    }

    @Test
    void checkFindNewsCommentsByIdShouldReturnStatusOkAndJson() throws Exception {
        doReturn(MockUtil.getDtoComments()).when(commentService).findCommentsByNewsId(1L, Pageable.unpaged());

        mockMvc.perform(get("/api/v1/news/1/comments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void checkCreateNewsShouldReturnStatusCreated() throws Exception {
        NewsDto dto = NewsDto.builder().title("Mock mvc").text("This is web mvc test").build();

        doNothing().when(newsService).save(dto);

        mockMvc.perform(post("/api/v1/news")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void checkUpdateNewsByIdShouldReturnStatusOkAndDto() throws Exception {
        NewsDto dto = NewsDto.builder().title("Mock mvc").text("This is web mvc test").build();
        doNothing().when(newsService).updateById(1L, dto);

        mockMvc.perform(patch("/api/v1/news/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void checkDeleteNewsByIdShouldReturnStatusNoContent() throws Exception {
        doNothing().when(newsService).removeById(1L);

        mockMvc.perform(delete("/api/v1/news/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void checkDeleteNewsByIdShouldReturnStatusNotFound() throws Exception {
        doThrow(NewsNotFoundException.class).when(newsService).removeById(1L);

        mockMvc.perform(delete("/api/v1/news/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void checkFindNewsWithFilterByTitleOrTextShouldReturnStatusOkAndJson() throws Exception {
        doReturn(MockUtil.getDtoNews()).when(newsService).findAllWithFilterByTitleOrText("title", "text");

        mockMvc.perform(get("/api/v1/news/filter")
                        .param("title", "title")
                        .param("text", "text"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}