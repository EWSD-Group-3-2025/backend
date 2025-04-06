package org.teamSmurfs.backend.features.dashboard.dto.tutor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.teamSmurfs.backend.features.dashboard.dto.DashboardTodayMeeting;
import org.teamSmurfs.backend.features.user.dto.StudentDashBoardDto;

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
