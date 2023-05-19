package ru.clevertec.newssystemmanagement.controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.newssystemmanagement.dto.CommentDto;
import ru.clevertec.newssystemmanagement.dto.NewsDto;
import ru.clevertec.newssystemmanagement.services.CommentService;
import ru.clevertec.newssystemmanagement.services.NewsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
@Tag(name = "news", description = "The News API")
public class NewsController {

    private final NewsService newsService;
    private final CommentService commentService;

    @Operation(description = "find all news with pagination and sorting by time (default)", tags = "news")
    @ApiResponse(
            responseCode = "200",
            content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = NewsDto.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<NewsDto>> findAllNews(@PageableDefault(size = 7, sort = "time") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(newsService.findAll(pageable));
    }

    @Operation(description = "find news by id", tags = {"news", "exception"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = NewsDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "News not found with such id",
                    content = {@Content(mediaType = "application/json")}
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<NewsDto> findNewsById(@PathVariable @Parameter(description = "unique news id") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(newsService.findById(id));
    }

    @Operation(description = "find comments by news id", tags = {"news", "comment", "exception"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CommentDto.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "News not found with such id",
                    content = {@Content(mediaType = "application/json")}
            )
    })
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentDto>> findNewsCommentsById(@PathVariable @Parameter(description = "unique news id") long id,
                                                                 @PageableDefault(size = 7, sort = "time") Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.findCommentsByNewsId(id, pageable));
    }

    @Operation(description = "create news", tags = "news")
    @ApiResponse(
            responseCode = "201",
            content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = NewsDto.class))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<NewsDto> createNews(@RequestBody NewsDto newsDto) {
        newsService.save(newsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newsDto);
    }

    @Operation(description = "update news by id", tags = {"news", "exception"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CommentDto.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "News not found with such id",
                    content = {@Content(mediaType = "application/json")}
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<NewsDto> updateNewsById(@PathVariable @Parameter(description = "unique news id") long id,
                                                  @RequestBody NewsDto newsDto) {
        newsService.updateById(id, newsDto);
        return ResponseEntity.status(HttpStatus.OK).body(newsDto);
    }

    @Operation(description = "delete news by id", tags = {"news", "exception"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = CommentDto.class))
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "News not found with such id",
                    content = {@Content(mediaType = "application/json")}
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNewsById(@PathVariable @Parameter(description = "unique news id") long id) {
        newsService.removeById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "find news with filter by title or text", tags = {"news", "filter"})
    @ApiResponse(
            responseCode = "200",
            content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CommentDto.class))
                    )
            }
    )
    @GetMapping("/filter")
    public ResponseEntity<List<NewsDto>> findNewsWithFilterByTitleOrText(@RequestParam(required = false) @Parameter(description = "keyword for search by title") String title,
                                                                         @RequestParam(required = false) @Parameter(description = "keyword for search by text") String text) {
        return ResponseEntity.status(HttpStatus.OK).body(newsService.findAllWithFilterByTitleOrText(title, text));
    }
}
