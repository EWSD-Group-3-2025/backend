package org.teamSmurfs.backend.features.library.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.teamSmurfs.backend.features.library.dto.BookDto;
import org.teamSmurfs.backend.features.library.dto.LibraryRequest;
import org.teamSmurfs.backend.features.library.service.LibraryService;
import org.teamSmurfs.backend.features.request.RequestUtils;
import org.teamSmurfs.backend.features.response.dto.ApiResponse;
import org.teamSmurfs.backend.features.response.utils.ResponseUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/${api.base.path}/libraries")
@RequiredArgsConstructor
@Slf4j
public class LibraryController {
    private final LibraryService libraryService;

    @PostMapping("/books")
    public ResponseEntity<ApiResponse> createBook(
            @RequestHeader(value = "Authorization", required = false) final String authHeader,
            @Validated @RequestBody final LibraryRequest createLibraryRequest,
            final HttpServletRequest request
    ) {
        log.info("Creating book with name: {}", createLibraryRequest.getBookName());

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        String maskedAuthHeader = authHeader != null ? authHeader.substring(0, Math.min(10, authHeader.length())) + "***" : "N/A";
        log.info("Processing blog for Authorization: {}", maskedAuthHeader);

        this.libraryService.createBook(createLibraryRequest, authHeader);

        log.info("Created book successfully: {}", createLibraryRequest.getBookName());

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .data(true)
                .message("Book created successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @GetMapping("/books")
    public ResponseEntity<ApiResponse> retrieveAllBooks(
            final HttpServletRequest request
    ) {
        log.info("Retrieving all books from the library");

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        List<BookDto> books = this.libraryService.retrieveAllBooks();

        log.info("Retrieved all books from the library successfully: {}", (books != null) ? books.size() : 0);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(books != null ? books : Collections.emptyList())
                .message("Books retrieved successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<ApiResponse> retrieveBook(
            @PathVariable(value = "id") final Long id,
            final HttpServletRequest request
    ) {
        log.info("Retrieving book with id {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        BookDto library = this.libraryService.retrieveBook(id);

        log.info("Retrieved library successfully: {}", library);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .data(library)
                .message("Library created successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<ApiResponse> updateBook(
            @RequestHeader(value = "Authorization", required = false) final String authHeader,
            @PathVariable(value = "id") final Long id,
            @Validated @RequestBody final LibraryRequest updateLibraryRequest,
            final HttpServletRequest request

    ) {
        log.info("Updating book with id {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        String maskedAuthHeader = authHeader != null ? authHeader.substring(0, Math.min(10, authHeader.length())) + "***" : "N/A";
        log.info("Processing blog for Authorization: {}", maskedAuthHeader);

        this.libraryService.updateBook(id, updateLibraryRequest, authHeader);

        log.info("Updated book successfully: {}", updateLibraryRequest.getBookName());

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.NO_CONTENT.value())
                .data(true)
                .message("Book updated successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<ApiResponse> deleteBook(
            @PathVariable(value = "id") final Long id,
            final HttpServletRequest request
    ) {
        log.info("Deleting book with id {}", id);

        double requestStartTime = RequestUtils.extractRequestStartTime(request);

        this.libraryService.deleteBook(id);

        log.info("Deleted book successfully with id {}", id);

        ApiResponse successResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.NO_CONTENT.value())
                .data(true)
                .message("Book deleted successfully")
                .build();

        return ResponseUtil.buildResponse(request, successResponse, requestStartTime);
    }
}
