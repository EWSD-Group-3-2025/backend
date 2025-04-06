package org.teamSmurfs.backend.features.dashboard.dto.student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDashboardCount {
    private int newMessageCountForToday;
    private int meetingCountForToday;
    private int eventCountForToday;
}
