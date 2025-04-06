package org.teamSmurfs.backend.features.library.service;

import org.teamSmurfs.backend.features.library.dto.BookDto;
import org.teamSmurfs.backend.features.library.dto.LibraryRequest;

import java.util.List;

public interface LibraryService {
    void createBook(final LibraryRequest libraryRequest, final String authHeader);
    void updateBook(final Long id, final LibraryRequest libraryRequest, final String authHeader);
    List<BookDto> retrieveAllBooks();
    BookDto retrieveBook(final Long id);
    void deleteBook(final Long id);
}
