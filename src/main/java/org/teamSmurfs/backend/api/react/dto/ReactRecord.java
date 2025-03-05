package org.teamSmurfs.backend.api.react.dto;

public record ReactRecord(
    Long id,
    Long authorId,
    String react,
    Long entityId,
    Integer entityType,
    String createdAt,
    String updatedAt
) {}
