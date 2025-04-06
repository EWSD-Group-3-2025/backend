package org.teamSmurfs.backend.features.library.dto;

public record BookRecord(
    Long id,
    String name,
    String url,
    Integer categoryId,
    String difficultyLevel,
    Integer rating,
    String organizationName,
    String organizationUrl,
    String description,
    Long uploaderId,
    String uploaderName,
    String createdAt,
    String updatedAt
) {}
