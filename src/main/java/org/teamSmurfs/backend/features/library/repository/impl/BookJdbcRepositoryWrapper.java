package org.teamSmurfs.backend.features.library.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.teamSmurfs.backend.config.exception.EntityNotFoundException;
import org.teamSmurfs.backend.features.library.dto.BookRecord;
import org.teamSmurfs.backend.features.library.mapper.BookRowMapper;
import org.teamSmurfs.backend.features.library.model.Book;
import org.teamSmurfs.backend.features.library.repository.BookJdbcRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class BookJdbcRepositoryWrapper implements BookJdbcRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final BookRowMapper BOOK_ROW_MAPPER = new BookRowMapper();

    private static final String INSERT_QUERY = """
        INSERT INTO book (name, url, category_id, difficulty_level, rating, organization_name, organization_url, description, uploader_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

    private static final String FIND_ALL_QUERY = """
        SELECT 
            b.id, b.name, b.url, b.category_id, b.difficulty_level, b.rating, b.organization_name, b.organization_url, b.description, b.uploader_id, u.name AS uploader_name, b.created_at, b.updated_at
        FROM
            book b
        LEFT JOIN
            user u ON u.id = b.uploader_id
    """;

    private static final String UPDATE_QUERY = """
        UPDATE book set name = ?, url = ?, category_id = ?, difficulty_level = ?, rating = ?, organization_name = ?, organization_url = ?, description = ?, updated_at = ? WHERE id = ?
    """;

    private static final String FIND_BY_ID_QUERY = """
        SELECT 
            b.id, b.name, b.url, b.category_id, b.difficulty_level, b.rating, b.organization_name, b.organization_url, b.description, b.uploader_id, u.name AS uploader_name, b.created_at, b.updated_at        
        FROM
            book b
        LEFT JOIN
            user u ON u.id = b.uploader_id
        where 
            b.id = ?
    """;

    private static final String DELETE_QUERY = """
        DELETE FROM book WHERE id = ?
    """;

    public BookJdbcRepositoryWrapper(final JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }
    @Override
    public void createBook(final Book bookRequest) {
        final LocalDateTime now = LocalDateTime.now();
        jdbcTemplate.update(INSERT_QUERY,
                bookRequest.getName(), bookRequest.getUrl(), bookRequest.getCategoryId(), bookRequest.getDifficultyLevel(), bookRequest.getRating(), bookRequest.getOrganizationName(), bookRequest.getOrganizationUrl(), bookRequest.getDescription(), bookRequest.getUploaderId(), now, now
        );
    }

    @Override
    public void updateBook(final Long id, final Book bookRequest) {
        final LocalDateTime now = LocalDateTime.now();

        jdbcTemplate.update(UPDATE_QUERY,
                bookRequest.getName(), bookRequest.getUrl(), bookRequest.getCategoryId(), bookRequest.getDifficultyLevel(), bookRequest.getRating(), bookRequest.getOrganizationName(), bookRequest.getOrganizationUrl(), bookRequest.getDescription(), now, id
        );
    }

    @Override
    public List<BookRecord> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, BOOK_ROW_MAPPER);
    }

    @Override
    public Optional<BookRecord> findBookById(final Long id) {
        return jdbcTemplate.query(FIND_BY_ID_QUERY, BOOK_ROW_MAPPER, id)
                .stream().findFirst();
    }

    @Override
    public void deleteBook(final Long id) {
        int rowsAffected = this.jdbcTemplate.update(DELETE_QUERY, id);

        if (rowsAffected == 0) {
            throw new EntityNotFoundException("No reacts found for ID: " + id);
        }
    }
}
