package org.teamSmurfs.backend.features.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.teamSmurfs.backend.features.meeting.model.Meeting;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> findByStartTimeBetweenAndHostId(LocalDateTime startDate, LocalDateTime endDate, Long hostId);

    @Query("SELECT COUNT(m) FROM Meeting m WHERE m.host.id = :hostId AND m.startTime BETWEEN :startOfDay AND :endOfDay")
    int countMeetingsForToday(@Param("hostId") Long hostId, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT m FROM Meeting m JOIN m.participants p WHERE p.id = :studentId AND m.startTime BETWEEN :startOfDay AND :endOfDay")
    List<Meeting> findByStartTimeBetweenAndParticipantsId(@Param("startOfDay") LocalDateTime startOfDay,
                                                          @Param("endOfDay") LocalDateTime endOfDay,
                                                          @Param("studentId") Long studentId);


    @Query("SELECT COUNT(m) FROM Meeting m JOIN m.participants p WHERE p.id = :studentId AND m.startTime BETWEEN :startOfDay AND :endOfDay")
    int countMeetingsByStartTimeBetweenAndParticipantsId(@Param("startOfDay") LocalDateTime startOfDay,
                                                          @Param("endOfDay") LocalDateTime endOfDay,
                                                          @Param("studentId") Long studentId);

}
