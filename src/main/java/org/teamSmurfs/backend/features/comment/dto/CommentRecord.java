package org.teamSmurfs.backend.features.comment.dto;

public record CommentRecord(
    Long id,
    Long blogId,
    Long commenterId,
    String commenterName,
    String commentText,
    String createdAt,
    String updatedAt
) {}
