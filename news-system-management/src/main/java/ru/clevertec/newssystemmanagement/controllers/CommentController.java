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
import ru.clevertec.newssystemmanagement.services.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
@Tag(name = "comment", description = "The Comment API")
public class CommentController {

    private final CommentService commentService;


    @Operation(description = "find all comments with pagination and sorting by news id, time (default)", tags = "comment")
    @ApiResponse(
            responseCode = "200",
            content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CommentDto.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<CommentDto>> findAllComments(@PageableDefault(size = 7, sort = {"newsId", "time"}) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.findAll(pageable));
    }

    @Operation(description = "find comment by id", tags = {"comment", "exception"})
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentDto.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Comment not found with such id",
                    content = {@Content(mediaType = "application/json")}
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> findCommentById(@PathVariable @Parameter(description = "unique comment id") long id) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.findById(id));
    }

    @Operation(description = "create new comment", tags = "comment")
    @ApiResponse(
            responseCode = "201",
            content = {
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CommentDto.class))
                    )
            }
    )
    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto) {
        commentService.save(commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentDto);
    }

    @Operation(description = "update comment by id", tags = {"comment", "exception"})
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
                    description = "Comment not found with such id",
                    content = {@Content(mediaType = "application/json")}
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CommentDto> updateCommentById(@PathVariable @Parameter(description = "unique comment id") long id,
                                                        @RequestBody CommentDto commentDto) {
        commentService.updateById(id, commentDto);
        return ResponseEntity.status(HttpStatus.OK).body(commentDto);
    }

    @Operation(description = "delete comment by id", tags = {"comment", "exception"})
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
                    description = "Comment not found with such id",
                    content = {@Content(mediaType = "application/json")}
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCommentById(@PathVariable @Parameter(description = "unique comment id") long id) {
        commentService.removeById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(description = "find comments with filter by text or username", tags = {"comment", "filter"})
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
    public ResponseEntity<List<CommentDto>> findCommentsWithFilterByTitleOrText(@RequestParam(required = false) @Parameter(description = "text for search") String text,
                                                                                @RequestParam(required = false) @Parameter(description = "username for search") String username) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.findCommentsWithFilterByTextOrUsername(text, username));
    }
}
