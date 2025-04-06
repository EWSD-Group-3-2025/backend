package org.teamSmurfs.backend.features.meeting.service;
import org.teamSmurfs.backend.features.meeting.dto.MeetingDto;
import org.teamSmurfs.backend.features.meeting.dto.MeetingResponse;
import org.teamSmurfs.backend.features.dashboard.dto.DashboardTodayMeeting;

import java.util.List;

public interface MeetingService {
    MeetingResponse create(MeetingDto meetingDto);
    MeetingResponse update(Long id, MeetingDto meetingDto);
    void delete(Long id);
    MeetingResponse getById(Long id, String authHeader);
    List<MeetingResponse> getAll(String authHeader);
    List<DashboardTodayMeeting> getTodayMeetingsForTutor(Long tutorId);
    List<DashboardTodayMeeting> getTodayMeetingsForStudent(final Long studentId);
    void markMeetingAsDone(Long id);
}
