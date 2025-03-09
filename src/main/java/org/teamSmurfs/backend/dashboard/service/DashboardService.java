package org.teamSmurfs.backend.dashboard.service;

import org.teamSmurfs.backend.api.user.dto.StudentDto;
import org.teamSmurfs.backend.api.user.dto.TutorDto;
import org.teamSmurfs.backend.dashboard.dto.AdminDashboardDto;

import java.util.List;

public interface DashboardService {

    AdminDashboardDto getAdminDashboardData();
    TutorDto getTutorByStudentId(Long userId);
    List<StudentDto> getStudentsByTutorId(Long userId);
}
