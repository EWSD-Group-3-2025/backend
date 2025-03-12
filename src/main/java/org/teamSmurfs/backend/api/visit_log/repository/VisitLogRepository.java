package org.teamSmurfs.backend.api.visit_log.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.teamSmurfs.backend.api.user.dto.UserDto;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.visit_log.dto.VisitLogDto;
import org.teamSmurfs.backend.api.visit_log.model.VisitLog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {

    @Query("""
    SELECT v.user FROM VisitLog v
    WHERE v.createdAt = (SELECT MAX(v2.createdAt) FROM VisitLog v2 WHERE v2.user = v.user)
    AND v.createdAt < :cutoffDate
    AND v.user.inactive = false
    AND v.user.status = true
""")
    List<User> findInactiveUsers(LocalDateTime cutoffDate);

    @Query("""
    SELECT new org.teamSmurfs.backend.api.visit_log.dto.VisitLogDto(
        v.routeName, v.browserName, v.user.username, v.pageName
    )\s
    FROM VisitLog v
""")
    List<VisitLogDto> findAllVisitLogsWithUsername();


    @Query("SELECT v.user FROM VisitLog v GROUP BY v.user ORDER BY COUNT(v) DESC LIMIT 20")
    List<User> getMostVisitedUsers(); // for get only 20 most visited user count 

    @Query("SELECT v.user, COUNT(v) FROM VisitLog v GROUP BY v.user ORDER BY COUNT(v) DESC")
	List<User> getMostActiveUsers(Pageable pageable);

    @Query("SELECT v.user FROM VisitLog v WHERE v.createdAt BETWEEN :startDate AND :endDate GROUP BY v.user")
    List<User> findInactiveUsersBetweenDates(
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );

	List<VisitLog> findByUserId(Long id);

}
