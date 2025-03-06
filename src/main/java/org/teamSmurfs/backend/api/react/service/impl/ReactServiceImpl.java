package org.teamSmurfs.backend.api.react.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.api.react.dto.ReactRecord;
import org.teamSmurfs.backend.api.react.repository.ReactJdbcRepository;
import org.teamSmurfs.backend.api.react.service.ReactService;
import org.teamSmurfs.backend.api.user.dto.UserDto;
import org.teamSmurfs.backend.api.user.utils.UserUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReactServiceImpl implements ReactService {

    private final ReactJdbcRepository jdbcRepository;
    private final UserUtil userUtil;

    @Override
    public void handleReaction(final String authHeader, final Long entityId, final Integer entityType,
                               final String react, final boolean isAddReaction) {
        final UserDto userDto = this.userUtil.getCurrentUserDto(authHeader);

        if (isAddReaction) {
            this.validateReact(react);
            this.jdbcRepository.giveReaction(userDto.getId(), react, entityId, entityType);
            log.info("Adding reaction '{}' to entity with ID {}", react, entityId);
        } else {
            this.jdbcRepository.undoReaction(userDto.getId(), entityId, entityType);
            log.info("Removing reaction from entity with ID {}", entityId);
        }
    }

    @Override
    public boolean isReactionExists(final String authHeader, final Long entityId, final Integer entityType) {
        final UserDto userDto = this.userUtil.getCurrentUserDto(authHeader);
        final List<ReactRecord> existingReactions = this.jdbcRepository.findByEntityIdAndEntityType(entityId, entityType);
        return existingReactions.stream()
                .anyMatch(reactRecord -> reactRecord.authorId().equals(userDto.getId()));
    }

    private void validateReact(String react) {
        if (react == null || react.trim().isEmpty()) {
            throw new IllegalArgumentException("Reaction cannot be empty.");
        }
    }
}
