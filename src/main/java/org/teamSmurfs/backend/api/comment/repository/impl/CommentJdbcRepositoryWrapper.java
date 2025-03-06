package org.teamSmurfs.backend.api.comment.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.teamSmurfs.backend.api.comment.dto.CommentRecord;
import org.teamSmurfs.backend.api.comment.mapper.CommentRowMapper;
import org.teamSmurfs.backend.api.comment.repository.CommentJdbcRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class CommentJdbcRepositoryWrapper implements CommentJdbcRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final CommentRowMapper COMMENT_ROW_MAPPER = new CommentRowMapper();

    public CommentJdbcRepositoryWrapper(final JdbcTemplate jdbcTemplate) { this.jdbcTemplate = jdbcTemplate; }

    private static final String FIND_BY_BLOG_ID = """
        SELECT
            c.id,
            c.blog_id,
            u.name AS commenter_name,
            c.comment_text,
            c.created_at,
            c.updated_at
        FROM
            comment c
        LEFT JOIN
            user u ON u.id = c.commenter_id
        WHERE
            c.blog_id=?
    """;

    private static final String FIND_BY_ID_QUERY = """
        SELECT
            c.id,
            c.blog_id,
            u.name AS commenter_name,
            c.comment_text,
            c.created_at,
            c.updated_at
        FROM
            comment c
        LEFT JOIN
            user u ON u.id = c.commenter_id
        WHERE
            c.id=?
    """;

    @Override
    public List<CommentRecord> findByBlogId(Long blogId) {
        return jdbcTemplate.query(FIND_BY_BLOG_ID, COMMENT_ROW_MAPPER, blogId);
    }

    @Override
    public Optional<CommentRecord> findById(Long id) {
        return jdbcTemplate.query(FIND_BY_ID_QUERY, COMMENT_ROW_MAPPER, id)
                .stream().findFirst();
    }
}
