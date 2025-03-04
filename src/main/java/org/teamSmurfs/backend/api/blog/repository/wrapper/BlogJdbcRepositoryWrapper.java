package org.teamSmurfs.backend.api.blog.repository.wrapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.teamSmurfs.backend.api.blog.dto.BlogRecord;
import org.teamSmurfs.backend.api.blog.mapper.BlogRowMapper;
import org.teamSmurfs.backend.api.blog.repository.BlogJdbcRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class BlogJdbcRepositoryWrapper implements BlogJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public BlogJdbcRepositoryWrapper(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final BlogRowMapper BLOG_ROW_MAPPER = new BlogRowMapper();

    private static final String FIND_ALL_QUERY = """
        SELECT id, author_id, title, content FROM blogs
    """;

//    private static final String FIND_BLOGS_FOR_THIS_USER_QUERY = """
//        SELECT
//            b.id AS id,
//            u.name AS author_name,
//            b.title AS title,
//            b.content AS content,
//            b.created_at AS created_at,
//            b.updated_at AS updated_at
//        FROM
//            blog b
//        LEFT JOIN
//            user u ON u.id = b.author_id
//        WHERE
//            b.author_id = ?
//    """;

    private static final String FIND_BLOGS_FOR_THIS_USER_QUERY = """
        SELECT
            b.id AS id,
            u.name AS author_name,
            b.title AS title,
            b.content AS content,
            b.created_at AS created_at,
            b.updated_at AS updated_at
        FROM
            blog b
        LEFT JOIN
            user u ON u.id = b.author_id
        WHERE 
            b.author_id IN (
                SELECT t.user_id 
                FROM tutor t
                JOIN allocation a ON t.id = a.tutor_id
                JOIN student s ON a.student_id = s.id
                WHERE s.user_id = ?
            )
            OR
            b.author_id IN (
                SELECT s.user_id 
                FROM student s
                JOIN allocation a ON s.id = a.student_id
                JOIN tutor t ON a.tutor_id = t.id
                WHERE t.user_id = ?
            )
            OR
            NOT EXISTS (SELECT 1 FROM student WHERE user_id = ?)
            AND
            NOT EXISTS (SELECT 1 FROM tutor WHERE user_id = ?)
    """;

    private static final String FIND_BY_ID_QUERY = """
        SELECT
            b.id AS id,
            u.name AS author_name,
            b.title AS title,
            b.content AS content,
            b.created_at AS created_at,
            b.updated_at AS updated_at
        FROM
            blog b
        LEFT JOIN
            user u ON u.id = b.author_id
        WHERE 
            b.id = ?
    """;

    private static final String FIND_BY_AUTHOR_ID_QUERY = """
        SELECT
            b.id AS id,
            u.name AS author_name,
            b.title AS title,
            b.content AS content,
            b.created_at AS created_at,
            b.updated_at AS updated_at
        FROM
            blog b
        LEFT JOIN
            user u ON u.id = b.author_id        
        WHERE 
            b.author_id = ?
    """;

    public List<BlogRecord> findAll() {
        return jdbcTemplate.query(FIND_ALL_QUERY, BLOG_ROW_MAPPER);
    }

    public List<BlogRecord> findBlogsForThisUser(final Long userId) {
        return jdbcTemplate.query(FIND_BLOGS_FOR_THIS_USER_QUERY, BLOG_ROW_MAPPER, userId, userId, userId, userId);
    }

    public Optional<BlogRecord> findById(final Long blogId) {
        return jdbcTemplate.query(FIND_BY_ID_QUERY, BLOG_ROW_MAPPER, blogId)
                .stream().findFirst();
    }

    public List<BlogRecord> findByAuthorId(final Long userId) {
        return jdbcTemplate.query(FIND_BY_AUTHOR_ID_QUERY, BLOG_ROW_MAPPER, userId);
    }
}
