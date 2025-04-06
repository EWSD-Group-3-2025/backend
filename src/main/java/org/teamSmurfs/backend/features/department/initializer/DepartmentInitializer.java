package org.teamSmurfs.backend.features.department.initializer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.teamSmurfs.backend.features.department.model.Department;
import org.teamSmurfs.backend.features.department.repository.DepartmentRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(1)
public class DepartmentInitializer implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Initializing departments...");

        List<String> defaultDepartments = List.of("Admin & Human Resources");

        defaultDepartments.forEach(departmentName -> 
            this.departmentRepository.findByName(departmentName)
                .orElseGet(() -> {
                    Department department = new Department(departmentName, 1L);
                    this.departmentRepository.save(department);
                    log.info("Inserted department: {}", departmentName);
                    return department;
                })
        );

        log.info("Department initialization completed.");
    }
}
