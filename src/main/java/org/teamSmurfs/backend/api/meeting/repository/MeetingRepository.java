package org.teamSmurfs.backend.api.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.api.meeting.model.Meeting;
import org.teamSmurfs.backend.api.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    List<Meeting> findByStartTimeBetweenAndHostId(LocalDateTime startDate, LocalDateTime endDate, Long hostId);



}
