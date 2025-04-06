package org.teamSmurfs.backend.features.comment.repository;

import org.teamSmurfs.backend.features.comment.dto.CommentRecord;

import java.util.List;
import java.util.Optional;

public interface CommentJdbcRepository {
    List<CommentRecord> findByBlogId(final Long blogId);
    Optional<CommentRecord> findById(final Long id);
    void deleteByBlogId(final Long blogId);
}
