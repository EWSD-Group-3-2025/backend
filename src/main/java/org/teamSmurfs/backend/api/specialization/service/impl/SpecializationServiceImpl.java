package org.teamSmurfs.backend.api.specialization.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.api.specialization.dto.CreateSpecializationRequest;
import org.teamSmurfs.backend.api.specialization.dto.SpecializationDto;
import org.teamSmurfs.backend.api.specialization.dto.UpdateSpecializationRequest;
import org.teamSmurfs.backend.api.specialization.model.Specialization;
import org.teamSmurfs.backend.api.specialization.repository.SpecializationRepository;
import org.teamSmurfs.backend.api.specialization.service.SpecializationService;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.user.repository.StaffRepository;
import org.teamSmurfs.backend.api.user.repository.UserRepository;
import org.teamSmurfs.backend.config.exception.EntityNotFoundException;
import org.teamSmurfs.backend.config.utils.EntityUtil;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Service
public class SpecializationServiceImpl implements SpecializationService {

    private final SpecializationRepository repository;
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final ModelMapper modelMapper;

    @Override
    public void create(final CreateSpecializationRequest createSpecializationRequest) {
        checkUserExists(createSpecializationRequest.getStaffId());
        List<Specialization> specializations = Arrays.stream(createSpecializationRequest.getNames())
                .map(name -> new Specialization(name, createSpecializationRequest.getStaffId()))
                .toList();
        this.repository.saveAll(specializations);
    }

    @Override
    public List<SpecializationDto> retrieveAll() {
        return repository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public SpecializationDto retrieveOne(final Long id) {
        return mapToDto(EntityUtil.getEntityById(this.repository, id));
    }

    @Override
    public SpecializationDto update(final Long id, final UpdateSpecializationRequest updateSpecializationRequest) {
        final Specialization specialization = EntityUtil.getEntityById(this.repository, id);
        if (updateSpecializationRequest.getName() != null && !specialization.getName().equals(updateSpecializationRequest.getName())) {
            specialization.setName(updateSpecializationRequest.getName());
        }
        if (updateSpecializationRequest.getStaffId() != null && !specialization.getStaffId().equals(updateSpecializationRequest.getStaffId())) {
            checkUserExists(updateSpecializationRequest.getStaffId());
            specialization.setStaffId(updateSpecializationRequest.getStaffId());
        }
        return mapToDto(this.repository.save(specialization));
    }

    @Override
    public void delete(final Long id) {
        EntityUtil.deleteEntity(this.repository, id, "Specialization");
    }

    private void checkUserExists(final Long userId) {
        User user = EntityUtil.getEntityById(this.userRepository, userId);
        if (!this.staffRepository.existsByUserId(user.getId())) {
            throw new EntityNotFoundException("Staff not found for User ID: " + user.getId());
        }
    }

    private SpecializationDto mapToDto(final Specialization specialization) {
        SpecializationDto specializationDto = modelMapper.map(specialization, SpecializationDto.class);
        specializationDto.setStaffName(EntityUtil.getEntityById(this.userRepository, specialization.getStaffId()).getName());
        return specializationDto;
    }
}
