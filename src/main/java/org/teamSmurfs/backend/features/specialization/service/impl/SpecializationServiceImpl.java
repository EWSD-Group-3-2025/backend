package org.teamSmurfs.backend.features.specialization.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.features.specialization.dto.CreateSpecializationRequest;
import org.teamSmurfs.backend.features.specialization.dto.SpecializationDto;
import org.teamSmurfs.backend.features.specialization.dto.UpdateSpecializationRequest;
import org.teamSmurfs.backend.features.specialization.model.Specialization;
import org.teamSmurfs.backend.features.specialization.repository.SpecializationRepository;
import org.teamSmurfs.backend.features.specialization.service.SpecializationService;
import org.teamSmurfs.backend.features.user.model.Tutor;
import org.teamSmurfs.backend.features.user.model.User;
import org.teamSmurfs.backend.features.user.repository.TutorRepository;
import org.teamSmurfs.backend.features.user.repository.UserRepository; // Ensure this repository exists
import org.teamSmurfs.backend.config.exception.EntityDeletionException;
import org.teamSmurfs.backend.config.exception.EntityNotFoundException;
import org.teamSmurfs.backend.config.utils.EntityUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SpecializationServiceImpl implements SpecializationService {

    private final SpecializationRepository repository;
    private final UserRepository userRepository; // Added UserRepository to fetch staff names
    private final TutorRepository tutorRepository;
    private final ModelMapper modelMapper;

    @Override
    public void create(final CreateSpecializationRequest createSpecializationRequest) {
    	
    	for (String specializationName : createSpecializationRequest.getNames()) {
            if (this.repository.existsByName(specializationName)) {  // Check if the specialization name already exists
                throw new IllegalArgumentException("SpecializationName name '" + specializationName + "' already exists.");
            }
        }
        List<Specialization> specializations = Arrays.stream(createSpecializationRequest.getNames())
                .map(name -> new Specialization(name, createSpecializationRequest.getStaffId()))
                .collect(Collectors.toList());

        // Save specialization entities in the repository
        this.repository.saveAll(specializations);
    }

    @Override
    public List<SpecializationDto> retrieveAll() {
        return repository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SpecializationDto retrieveOne(final Long id) {
        return mapToDto(EntityUtil.getEntityById(this.repository, id));
    }

    @Override
    public SpecializationDto update(final Long id, final UpdateSpecializationRequest updateSpecializationRequest) {
        // Retrieve specialization entity by id
        final Specialization specialization = EntityUtil.getEntityById(this.repository, id);

        // Update fields if necessary
        if (updateSpecializationRequest.getName() != null) {
            specialization.setName(updateSpecializationRequest.getName());
        }

        if (updateSpecializationRequest.getStaffId() != null) {
            specialization.setStaffId(updateSpecializationRequest.getStaffId());
        }

        // Save updated specialization
        return mapToDto(this.repository.save(specialization));
    }

    @Override
    public void delete(final Long id) {
        Specialization specialization = EntityUtil.getEntityById(this.repository, id);

        List<Tutor> tutors = this.tutorRepository.findBySpecializationId(specialization.getId());
        if (!tutors.isEmpty())
            throw new EntityDeletionException(
                    "Cannot delete Specialization because it is associated with " + tutors.size() + " Tutor(s).");

        EntityUtil.deleteEntity(this.repository, id, "Specialization");
    }

    private SpecializationDto mapToDto(final Specialization specialization) {
        SpecializationDto specializationDto = modelMapper.map(specialization, SpecializationDto.class);

        // Map staffName from staffId by fetching the corresponding user entity
        if (specialization.getStaffId() != null) {
            User staff = userRepository.findById(specialization.getStaffId())
                    .orElseThrow(() -> new EntityNotFoundException("Staff member not found for ID: " + specialization.getStaffId()));
            specializationDto.setStaffName(staff.getName());
        }

        return specializationDto;
    }
}
