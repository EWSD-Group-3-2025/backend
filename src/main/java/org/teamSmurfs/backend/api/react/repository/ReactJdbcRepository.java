package org.teamSmurfs.backend.api.react.repository;

import org.teamSmurfs.backend.api.react.dto.ReactRecord;

import java.util.List;

public interface ReactJdbcRepository {
    List<ReactRecord> findByEntityIdAndEntityType(final Long entityId, final Integer entityType);
    void giveReaction(final Long authorId, final String reaction, final Long entityId, final Integer entityType);
    void undoReaction(final Long authorId, final Long entityId, final Integer entityType);
}
