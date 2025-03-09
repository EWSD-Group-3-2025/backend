package org.teamSmurfs.backend.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardDto {
    private long totalUsers;
    private long assignedStudents;
    //private double assignedPercentage;
    private long activeTutors;
    private long totalMessages;
}
