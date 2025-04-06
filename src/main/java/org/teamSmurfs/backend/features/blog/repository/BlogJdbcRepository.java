package org.teamSmurfs.backend.features.blog.repository;

import org.teamSmurfs.backend.features.blog.dto.BlogRecord;

import java.util.List;
import java.util.Optional;

public interface BlogJdbcRepository {
    List<BlogRecord> findAll();
    List<BlogRecord> findBlogsForThisUser(final Long blogId);
    Optional<BlogRecord> findById(final Long blogId);
    List<BlogRecord> findByAuthorId(final Long userId);
}
