package org.teamSmurfs.backend.api.comment.repository;

import org.teamSmurfs.backend.api.comment.dto.CommentRecord;

import java.util.List;

public interface CommentJdbcRepository {
    List<CommentRecord> findByBlogId(final Long blogId);
}
