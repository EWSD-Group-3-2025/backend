package org.teamSmurfs.backend.features.visit_log.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.teamSmurfs.backend.features.user.model.User;
import org.teamSmurfs.backend.features.visit_log.dto.VisitLogDto;
import org.teamSmurfs.backend.features.visit_log.model.VisitLog;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitLogRepository extends JpaRepository<VisitLog, Long> {

    @Query("""
    SELECT v.user FROM VisitLog v
    WHERE v.createdAt = (SELECT MAX(v2.createdAt) FROM VisitLog v2 WHERE v2.user = v.user)
    AND v.createdAt < :cutoffDate
    AND v.user.inactive = false
    AND v.user.status = true
    AND v.user.deletedAt IS NULL
""")
    List<User> findInactiveUsers(LocalDateTime cutoffDate);

    @Query("""
    SELECT new org.teamSmurfs.backend.features.visit_log.dto.VisitLogDto(
        v.routeName, v.browserName, v.user.username, v.pageName
    )\s
    FROM VisitLog v
""")
    List<VisitLogDto> findAllVisitLogsWithUsername();


    @Query("SELECT v.user FROM VisitLog v WHERE v.user.deletedAt IS NULL GROUP BY v.user ORDER BY COUNT(v) DESC LIMIT 20")
    List<User> getMostVisitedUsers(); // for get only 20 most visited user count 

    @Query("SELECT v.user, COUNT(v) FROM VisitLog v WHERE v.user.deletedAt IS NULL GROUP BY v.user ORDER BY COUNT(v) DESC")
	List<User> getMostActiveUsers(Pageable pageable);

    /*@Query("SELECT v.user FROM VisitLog v WHERE v.createdAt BETWEEN :startDate AND :endDate GROUP BY v.user")
    List<User> findInactiveUsersBetweenDates(
        @Param("startDate") LocalDateTime startDate, 
        @Param("endDate") LocalDateTime endDate
    );*/
    
    @Query("""
    	    SELECT v.user FROM VisitLog v 
    	    WHERE v.createdAt = (
    	        SELECT MAX(v2.createdAt) FROM VisitLog v2 WHERE v2.user = v.user
    	    ) 
    	    AND v.createdAt BETWEEN :startDate AND :endDate
    	    AND v.user.deletedAt IS NULL
    	""")
    	List<User> findInactiveUsersBetweenDates(
    	    @Param("startDate") LocalDateTime startDate, 
    	    @Param("endDate") LocalDateTime endDate
    	);
    
    @Query("SELECT MAX(v.createdAt) FROM VisitLog v WHERE v.user.id = :userId")
    LocalDateTime findMostRecentVisitDate(@Param("userId") Long userId);

	List<VisitLog> findByUserId(Long id);

	List<VisitLog> findAllByOrderByIdDesc();

}
