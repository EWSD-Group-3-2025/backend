package org.teamSmurfs.backend.api.media.repository;

import java.time.LocalDateTime;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.teamSmurfs.backend.api.media.model.Media;

public interface MediaRepository extends JpaRepository<Media, Long> {
    @Query("SELECT COUNT(m) FROM Media m WHERE m.uploadedBy.id = :userId AND m.uploadedAt BETWEEN :startOfDay AND :endOfDay")
    int countDocumentsForToday(@Param("userId") Long userId, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
}
