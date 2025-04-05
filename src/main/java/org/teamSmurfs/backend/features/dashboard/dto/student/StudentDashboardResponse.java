package org.teamSmurfs.backend.features.dashboard.dto.student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.teamSmurfs.backend.features.dashboard.dto.DashboardChatMessage;
import org.teamSmurfs.backend.features.dashboard.dto.DashboardTodayMeeting;
import org.teamSmurfs.backend.features.user.dto.TutorDto;

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
