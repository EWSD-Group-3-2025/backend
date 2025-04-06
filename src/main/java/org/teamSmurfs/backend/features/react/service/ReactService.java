package org.teamSmurfs.backend.features.react.service;

public interface ReactService {
    boolean isReactionExists(final String authHeader, final Long entityId, final Integer entityType);
    void handleReaction(final String authHeader, final Long entityId, final Integer entityType, final String react, final boolean isAddReaction);
}
