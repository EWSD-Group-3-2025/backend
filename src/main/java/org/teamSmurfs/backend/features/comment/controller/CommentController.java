package org.teamSmurfs.backend.features.comment.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.teamSmurfs.backend.features.comment.dto.CommentDto;
import org.teamSmurfs.backend.features.comment.dto.CommentRequest;
import org.teamSmurfs.backend.features.comment.service.CommentService;
import org.teamSmurfs.backend.features.request.RequestUtils;
import org.teamSmurfs.backend.features.response.dto.ApiResponse;
import org.teamSmurfs.backend.features.response.utils.ResponseUtil;

@RestController
@RequestMapping("${api.base.path}/comments")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse> createComment(
            @RequestHeader(value = "Authorization", required = false) final String authHeader,
            @Valid @RequestBody final CommentRequest commentRequest,
            final HttpServletRequest request
    ) {
        log.info("Creating comment {}", commentRequest);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        this.commentService.createComment(authHeader, commentRequest);

        log.info("Created comment: {}", commentRequest);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(true)
                .message("Comment created successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateComment(
            @RequestHeader(value = "Authorization", required = false) final String authHeader,
            @Validated @RequestBody final CommentRequest commentRequest,
            @PathVariable(value = "id") final Long id,
            final HttpServletRequest request
    ) {
        log.info("Updating comment {}", commentRequest);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        this.commentService.updateComment(id, authHeader, commentRequest);

        log.info("Updated comment: {}", commentRequest);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(true)
                .message("Comment updated successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteComment(
            @PathVariable(value = "id") final Long id,
            final HttpServletRequest request
    ) {
        log.info("Deleting comment with ID: {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        this.commentService.deleteComment(id);

        log.info("Deleted comment: {}", id);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.NO_CONTENT.value())
                .data(true)
                .message("Comment deleted successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> retrieveOne(
            @PathVariable(value = "id") final Long id,
            final HttpServletRequest request
    ) {
        log.info("Retrieving comment with ID: {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        CommentDto comment = this.commentService.retrieveOne(id);

        log.info("Retrieved comment with ID: {}", id);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(comment)
                .message("Comment retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }
}
