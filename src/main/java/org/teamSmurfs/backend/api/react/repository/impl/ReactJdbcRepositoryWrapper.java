package org.teamSmurfs.backend.api.react.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.teamSmurfs.backend.api.react.dto.ReactRecord;
import org.teamSmurfs.backend.api.react.mapper.ReactRowMapper;
import org.teamSmurfs.backend.api.react.repository.ReactJdbcRepository;

import java.util.List;

@Repository
public class ReactJdbcRepositoryWrapper implements ReactJdbcRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final ReactRowMapper REACT_ROW_MAPPER = new ReactRowMapper();

    public ReactJdbcRepositoryWrapper(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String FIND_BY_ENTITY_QUERY = """
        SELECT id, author_id, react, entity_id, entity_type, created_at, updated_at 
        FROM react 
        WHERE entity_id = ? AND entity_type = ?
    """;

    private static final String INSERT_REACT_QUERY = """
        INSERT INTO react (author_id, react, entity_id, entity_type, created_at, updated_at)
        VALUES (?, ?, ?, ?, NOW(), NOW())
    """;

    private static final String DELETE_REACT_QUERY = """
        DELETE FROM react WHERE author_id = ? AND entity_id = ? AND entity_type = ?
    """;

    @Override
    public List<ReactRecord> findByEntityIdAndEntityType(final Long entityId, final Integer entityType) {
        return jdbcTemplate.query(FIND_BY_ENTITY_QUERY, REACT_ROW_MAPPER, entityId, entityType);
    }

    @Override
    public void giveReaction(final Long authorId, final String reaction, final Long entityId, final Integer entityType) {
        jdbcTemplate.update(INSERT_REACT_QUERY, authorId, reaction, entityId, entityType);
    }

    @Override
    public void undoReaction(final Long authorId, final Long entityId, final Integer entityType) {
        jdbcTemplate.update(DELETE_REACT_QUERY, authorId, entityId, entityType);
    }
}
