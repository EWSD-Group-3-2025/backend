package org.teamSmurfs.backend.api.meeting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.api.meeting.model.Meeting;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
}
