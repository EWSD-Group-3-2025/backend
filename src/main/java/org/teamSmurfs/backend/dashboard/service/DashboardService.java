package org.teamSmurfs.backend.dashboard.service;

import org.teamSmurfs.backend.api.user.dto.StudentDashBoardDto;
import org.teamSmurfs.backend.api.user.dto.StudentDto;
import org.teamSmurfs.backend.api.user.dto.TutorDto;
import org.teamSmurfs.backend.dashboard.dto.AdminDashboardDto;
import org.teamSmurfs.backend.dashboard.dto.TutorDashboardCount;

import java.util.List;

public interface DashboardService {

    AdminDashboardDto getAdminDashboardData();
    TutorDto getTutorByStudentId(Long userId);
    List<StudentDashBoardDto> getStudentsByTutorId(Long userId);
    List<StudentDto> getUnassignedStudentsByTutorUserId();
    TutorDashboardCount retrieveDashboardCountByTutorUserId(final Long userId);
}
