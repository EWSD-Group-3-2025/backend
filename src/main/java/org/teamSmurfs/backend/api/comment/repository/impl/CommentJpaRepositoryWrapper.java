package org.teamSmurfs.backend.api.comment.repository.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.teamSmurfs.backend.api.comment.model.Comment;
import org.teamSmurfs.backend.api.comment.repository.CommentJpaRepository;
import org.teamSmurfs.backend.config.exception.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class CommentJpaRepositoryWrapper {

    private final CommentJpaRepository repository;

    public Comment save(final Comment entity) { return this.repository.save(entity); }

    @Transactional
    public void deleteById(final Long entityId) {
        this.findOneWithNotFoundDetection(entityId);
        this.repository.deleteById(entityId);
    }

    @Transactional(readOnly = true)
    public Comment findOneWithNotFoundDetection(final Long entityId) {
        return this.repository.findById(entityId).orElseThrow(() -> new EntityNotFoundException("Comment not found with id " + entityId));
    }
}
