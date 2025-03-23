package org.teamSmurfs.backend.dashboard.dto.tutor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TutorDashboardCount {
    private int newMessageCountForToday;
    private int meetingCountForToday;
    private int documentCountForToday;
}
