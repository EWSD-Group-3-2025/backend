package org.teamSmurfs.backend.api.specialization.service;

import org.teamSmurfs.backend.api.specialization.dto.CreateSpecializationRequest;
import org.teamSmurfs.backend.api.specialization.dto.SpecializationDto;
import org.teamSmurfs.backend.api.specialization.dto.UpdateSpecializationRequest;

import java.util.List;

public interface SpecializationService {
    void create(final CreateSpecializationRequest createSpecializationRequest);
    List<SpecializationDto> retrieveAll();
    SpecializationDto retrieveOne(final Long id);
    SpecializationDto update(final Long id, final UpdateSpecializationRequest updateDepartmentRequest);
    void delete(final Long id);
}
