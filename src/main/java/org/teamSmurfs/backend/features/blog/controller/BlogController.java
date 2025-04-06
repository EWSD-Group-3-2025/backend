package org.teamSmurfs.backend.features.blog.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.teamSmurfs.backend.features.blog.dto.BlogDto;
import org.teamSmurfs.backend.features.blog.dto.BlogRequest;
import org.teamSmurfs.backend.features.blog.service.BlogService;
import org.teamSmurfs.backend.features.request.RequestUtils;
import org.teamSmurfs.backend.features.response.dto.ApiResponse;
import org.teamSmurfs.backend.features.response.utils.ResponseUtil;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("${api.base.path}/blogs")
@RequiredArgsConstructor
@Slf4j
public class BlogController {
    private final BlogService blogService;

    @PostMapping
    public ResponseEntity<ApiResponse> createBlog(
            @RequestHeader(value = "Authorization", required = false) final String authHeader,
            @Validated @RequestBody final BlogRequest blogRequest,
            final HttpServletRequest request
    ) {
        log.info("Creating blog with authenticated user.");

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        String maskedAuthHeader = authHeader != null ? authHeader.substring(0, Math.min(10, authHeader.length())) + "***" : "N/A";
        log.info("Processing blog for Authorization: {}", maskedAuthHeader);

        this.blogService.createBlog(authHeader, blogRequest);

        log.info("Created blog with authenticated user.");

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(true)
                .message("Blog created successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> retrieveBlogs(
            @RequestHeader(value = "Authorization", required = false) final String authHeader,
            @RequestParam(value = "fetchFeed", defaultValue = "false") final boolean fetchFeed,
            final HttpServletRequest request
    ) {
        log.info("Retrieving blogs for authenticated user.");

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        List<BlogDto> blogList;

        String maskedAuthHeader = authHeader != null ? authHeader.substring(0, Math.min(10, authHeader.length())) + "***" : "N/A";
        log.info("Processing reset password for Authorization: {}", maskedAuthHeader);

        if (fetchFeed) {
            blogList = this.blogService.retrieveBlogsForThisUser(authHeader);
        } else {
            blogList = this.blogService.retrieveBlogsByThisUser(authHeader);
        }

        log.info("Retrieved {} blogs for authenticated user successfully", (blogList != null) ? blogList.size() : 0);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(blogList != null ? blogList : Collections.emptyList())
                .message("Blog retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> updateBlog(
            @RequestHeader(value = "Authorization", required = false) final String authHeader,
            @Validated @RequestBody final BlogRequest blogRequest,
            @PathVariable(value = "id") final Long id,
            final HttpServletRequest request
    ) {
        log.info("Updating with authenticated user.");

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        String maskedAuthHeader = authHeader != null ? authHeader.substring(0, Math.min(10, authHeader.length())) + "***" : "N/A";
        log.info("Updating blog for Authorization: {}", maskedAuthHeader);

        this.blogService.updateBlog(authHeader, id, blogRequest);

        log.info("Updated blog: {}", blogRequest);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(true)
                .message("Blog updated successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBlog(
            @PathVariable(value = "id") final Long id,
            final HttpServletRequest request
    ) {
        log.info("Deleting blog with ID: {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        this.blogService.deleteBlog(id);

        log.info("Deleted blog: {}", id);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.NO_CONTENT.value())
                .data(true)
                .message("Blog deleted successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> retrieveOne(
            @PathVariable(value = "id") final Long id,
            final HttpServletRequest request
    ) {
        log.info("Retrieving blog with ID: {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        final BlogDto blog = this.blogService.retrieveOne(id);

        log.info("Retrieved blog: {}", blog);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(blog)
                .message("Blog retrieved with ID: " + id + " successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }
}
