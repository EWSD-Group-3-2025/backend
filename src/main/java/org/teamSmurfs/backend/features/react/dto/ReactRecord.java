package org.teamSmurfs.backend.features.react.dto;

public record ReactRecord(
    Long id,
    Long authorId,
    String react,
    Long entityId,
    Integer entityType,
    String createdAt,
    String updatedAt
) {}
