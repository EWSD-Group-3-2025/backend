package org.teamSmurfs.backend.api.department.service;

import org.teamSmurfs.backend.api.department.dto.CreateDepartmentRequest;
import org.teamSmurfs.backend.api.department.dto.DepartmentDto;
import org.teamSmurfs.backend.api.department.dto.UpdateDepartmentRequest;

import java.util.List;

public interface DepartmentService {
    void create(final CreateDepartmentRequest createDepartmentRequest);
    List<DepartmentDto> retrieveAll();
    DepartmentDto retrieveOne(final Long id);
    DepartmentDto update(final Long id, final UpdateDepartmentRequest updateDepartmentRequest);
    void delete(final Long id);
}
