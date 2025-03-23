package org.teamSmurfs.backend.dashboard.dto.student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.teamSmurfs.backend.api.user.dto.TutorDto;
import org.teamSmurfs.backend.dashboard.dto.DashboardChatMessage;
import org.teamSmurfs.backend.dashboard.dto.DashboardTodayMeeting;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDashboardResponse {
    private TutorDto tutorDto;
    private List<DashboardTodayMeeting> dashboardTodayMeetings;
    private StudentDashboardCount studentDashboardCount;
    private List<DashboardChatMessage> dashboardChatMessages;
}
