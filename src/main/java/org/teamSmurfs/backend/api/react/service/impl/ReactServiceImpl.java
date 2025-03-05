package org.teamSmurfs.backend.api.react.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.api.react.dto.CreateReactRequest;
import org.teamSmurfs.backend.api.react.dto.DeleteReactRequest;
import org.teamSmurfs.backend.api.react.dto.ReactRecord;
import org.teamSmurfs.backend.api.react.model.ReactEntityType;
import org.teamSmurfs.backend.api.react.repository.ReactJdbcRepository;
import org.teamSmurfs.backend.api.react.service.ReactService;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.user.repository.UserRepository;
import org.teamSmurfs.backend.api.blog.repository.BlogJpaRepository;
import org.teamSmurfs.backend.api.event.repository.EventRepository;
import org.teamSmurfs.backend.api.chat.repository.ChatMessageRepository;
import org.teamSmurfs.backend.api.comment.repository.CommentRepository;
import org.teamSmurfs.backend.config.exception.EntityNotFoundException;
import org.teamSmurfs.backend.config.utils.EntityUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReactServiceImpl implements ReactService {

    private final ReactJdbcRepository jdbcRepository;
    private final UserRepository userRepository;
    private final BlogJpaRepository blogJpaRepository;
    private final EventRepository eventRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final CommentRepository commentRepository;

    @Override
    public void createReact(final CreateReactRequest createReactRequest) {
        final User author = EntityUtil.getEntityById(this.userRepository, createReactRequest.getAuthorId());
        final ReactEntityType entityType = ReactEntityType.fromInt(createReactRequest.getEntityType());
        Object entity = getEntityById(entityType, createReactRequest.getEntityId());

        this.handleReaction(author.getId(), createReactRequest.getReact(), createReactRequest.getEntityId(), entityType.getValue(), true);
    }

    @Override
    public void deleteReact(final DeleteReactRequest deleteReactRequest) {

        this.validateReactionOwnership(deleteReactRequest.getAuthorId(), deleteReactRequest.getEntityId(), deleteReactRequest.getEntityType());

        final User author = EntityUtil.getEntityById(this.userRepository, deleteReactRequest.getAuthorId());
        final ReactEntityType entityType = ReactEntityType.fromInt(deleteReactRequest.getEntityType());
        Object entity = getEntityById(entityType, deleteReactRequest.getEntityId());

        this.handleReaction(author.getId(), null, deleteReactRequest.getEntityId(), entityType.getValue(), false);
    }

    private void validateReactionOwnership(final Long authorId, final Long entityId, final Integer entityType) {
        final List<ReactRecord> existingReactions = jdbcRepository.findByEntityIdAndEntityType(entityId, entityType);
        final boolean reactionExists = existingReactions.stream()
                .anyMatch(reactRecord -> reactRecord.authorId().equals(authorId));

        if (!reactionExists) {
            throw new EntityNotFoundException("No reaction found from this author for this entity");
        }
    }

    private Object getEntityById(final ReactEntityType entityType, final Long entityId) {
        switch (entityType) {
            case BLOG -> {
                return EntityUtil.getEntityById(this.blogJpaRepository, entityId);
            }
            case EVENT -> {
                return EntityUtil.getEntityById(this.eventRepository, entityId);
            }
            case CHAT -> {
                return EntityUtil.getEntityById(this.chatMessageRepository, entityId);
            }
            case COMMENT -> {
                return EntityUtil.getEntityById(this.commentRepository, entityId);
            }
            default -> throw new IllegalArgumentException("Invalid entity type: " + entityType);
        }
    }
    
    private void handleReaction(final Long authorId, final String react, final Long entityId, final Integer entityType, final boolean isAddReaction) {
        if (isAddReaction) {
            log.info("Adding reaction '{}' to entity with ID {}", react, entityId);
            this.jdbcRepository.giveReaction(authorId, react, entityId, entityType);
        } else {
            log.info("Removing reaction from entity with ID {}", entityId);
            this.jdbcRepository.undoReaction(authorId, entityId, entityType);
        }
    }
}
