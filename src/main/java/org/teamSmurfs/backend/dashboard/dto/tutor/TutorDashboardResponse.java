package org.teamSmurfs.backend.dashboard.dto.tutor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.teamSmurfs.backend.api.user.dto.StudentDashBoardDto;
import org.teamSmurfs.backend.dashboard.dto.DashboardTodayMeeting;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TutorDashboardResponse {
    private List<StudentDashBoardDto> students;
    private TutorDashboardCount tutorDashboardCount;
    private List<DashboardTodayMeeting> dashboardTodayMeetings;
}
