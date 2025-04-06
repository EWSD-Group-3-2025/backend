package org.teamSmurfs.backend.features.dashboard.service;

import org.teamSmurfs.backend.features.dashboard.dto.AdminDashboardDto;
import org.teamSmurfs.backend.features.dashboard.dto.student.StudentDashboardCount;
import org.teamSmurfs.backend.features.dashboard.dto.tutor.TutorDashboardCount;
import org.teamSmurfs.backend.features.user.dto.StudentDashBoardDto;
import org.teamSmurfs.backend.features.user.dto.StudentDto;
import org.teamSmurfs.backend.features.user.dto.TutorDto;

import java.util.List;

public interface DashboardService {

    AdminDashboardDto getAdminDashboardData();
    TutorDto getTutorByStudentId(Long userId);
    List<StudentDashBoardDto> getStudentsByTutorId(Long userId);
    List<StudentDto> getUnassignedStudentsByTutorUserId();
    TutorDashboardCount retrieveDashboardCountByTutorUserId(final Long userId);
    StudentDashboardCount retrieveDashboardCountByStudentUserId(final Long userId);
}
