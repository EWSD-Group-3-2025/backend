package org.teamSmurfs.backend.features.comment.service;

import org.teamSmurfs.backend.features.comment.dto.CommentDto;
import org.teamSmurfs.backend.features.comment.dto.CommentRequest;

public interface CommentService {
    void createComment(final String authHeader, final CommentRequest commentRequest);
    void updateComment(final Long id, final String authHeader, final CommentRequest commentRequest);
    void deleteComment(final Long id);
    CommentDto retrieveOne(final Long id);
}
