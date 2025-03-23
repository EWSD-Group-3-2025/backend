package org.teamSmurfs.backend.api.comment.service;

import org.teamSmurfs.backend.api.comment.dto.CommentDto;
import org.teamSmurfs.backend.api.comment.dto.CommentRequest;

import java.util.List;

public interface CommentService {
    void createComment(final String authHeader, final CommentRequest commentRequest);
    void updateComment(final Long id, final String authHeader, final CommentRequest commentRequest);
    void deleteComment(final Long id);
    CommentDto retrieveOne(final Long id);
}
