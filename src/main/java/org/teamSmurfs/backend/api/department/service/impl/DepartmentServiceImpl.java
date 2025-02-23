package org.teamSmurfs.backend.api.department.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.teamSmurfs.backend.api.department.dto.CreateDepartmentRequest;
import org.teamSmurfs.backend.api.department.dto.DepartmentDto;
import org.teamSmurfs.backend.api.department.dto.UpdateDepartmentRequest;
import org.teamSmurfs.backend.api.department.model.Department;
import org.teamSmurfs.backend.api.department.repository.DepartmentRepository;
import org.teamSmurfs.backend.api.department.service.DepartmentService;
import org.teamSmurfs.backend.api.user.model.Staff;
import org.teamSmurfs.backend.api.user.model.User;
import org.teamSmurfs.backend.api.user.repository.StaffRepository;
import org.teamSmurfs.backend.api.user.repository.UserRepository;
import org.teamSmurfs.backend.config.exception.EntityDeletionException;
import org.teamSmurfs.backend.config.exception.EntityNotFoundException;
import org.teamSmurfs.backend.config.utils.EntityUtil;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository repository;
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final ModelMapper modelMapper;

    @Override
    public void create(final CreateDepartmentRequest createDepartmentRequest) {
        checkUserExists(createDepartmentRequest.getStaffId());
        List<Department> departments = Arrays.stream(createDepartmentRequest.getNames())
                .map(name -> new Department(name, createDepartmentRequest.getStaffId()))
                .toList();
        this.repository.saveAll(departments);
    }

    @Override
    public List<DepartmentDto> retrieveAll() {
        return repository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public DepartmentDto retrieveOne(final Long id) {
        return mapToDto(EntityUtil.getEntityById(this.repository, id));
    }

    @Override
    public DepartmentDto update(final Long id, final UpdateDepartmentRequest updateDepartmentRequest) {
        final Department department = EntityUtil.getEntityById(this.repository, id);
        if (updateDepartmentRequest.getName() != null && !department.getName().equals(updateDepartmentRequest.getName())) {
            department.setName(updateDepartmentRequest.getName());
        }
        if (updateDepartmentRequest.getStaffId() != null && !department.getStaffId().equals(updateDepartmentRequest.getStaffId())) {
            checkUserExists(updateDepartmentRequest.getStaffId());
            department.setStaffId(updateDepartmentRequest.getStaffId());
        }
        return mapToDto(this.repository.save(department));
    }

    @Override
    public void delete(final Long id) {
        Department department = EntityUtil.getEntityById(this.repository, id);

        List<Staff> staffList = this.staffRepository.findByDepartmentId(department.getId());
        if (!staffList.isEmpty())
            throw new EntityDeletionException(
                    "Cannot delete Department because it is associated with " + staffList.size() + " Staff(s).");

        EntityUtil.deleteEntity(this.repository, id, "Department");
    }

    private void checkUserExists(final Long userId) {
        User user = EntityUtil.getEntityById(this.userRepository, userId);
        if (!this.staffRepository.existsByUserId(user.getId())) {
            throw new EntityNotFoundException("Staff not found for User ID: " + user.getId());
        }
    }

    private DepartmentDto mapToDto(final Department department) {
        DepartmentDto departmentDto = modelMapper.map(department, DepartmentDto.class);
        departmentDto.setStaffName(EntityUtil.getEntityById(this.userRepository, department.getStaffId()).getName());
        return departmentDto;
    }
}
