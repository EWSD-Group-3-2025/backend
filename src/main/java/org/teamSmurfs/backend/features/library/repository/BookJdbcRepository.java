package org.teamSmurfs.backend.features.library.repository;

import org.teamSmurfs.backend.features.library.dto.BookRecord;
import org.teamSmurfs.backend.features.library.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookJdbcRepository {
    void createBook(final Book bookRequest);
    void updateBook(final Long id, final Book bookRequest);
    List<BookRecord> findAll();
    Optional<BookRecord> findBookById(final Long id);
    void deleteBook(final Long id);
}
