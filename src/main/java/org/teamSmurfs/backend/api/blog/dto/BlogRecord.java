package org.teamSmurfs.backend.api.blog.dto;

public record BlogRecord(
    Long id,
    String authorName,
    String title,
    String content,
    String createdAt,
    String updatedAt
) {}
