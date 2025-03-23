package org.teamSmurfs.backend.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardTodayMeeting {
    private Long id;
    private String hostName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;
    private Integer meetingType;
    private String link;
    private String location;
    private LocalDateTime createdAt;
    private boolean isDone;
}
