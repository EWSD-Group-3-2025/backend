package org.teamSmurfs.backend.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.teamSmurfs.backend.api.user.dto.StudentDashBoardDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TutorDashboardResponse {
    private List<StudentDashBoardDto> students;
    private TutorDashboardCount tutorDashboardCount;
}
