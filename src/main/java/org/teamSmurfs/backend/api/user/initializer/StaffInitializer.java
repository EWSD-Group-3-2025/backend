package org.teamSmurfs.backend.api.user.initializer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.teamSmurfs.backend.api.department.repository.DepartmentRepository;
import org.teamSmurfs.backend.api.user.model.Staff;
import org.teamSmurfs.backend.api.user.repository.StaffRepository;
import org.teamSmurfs.backend.api.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(4)
public class StaffInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Initializing staff...");

        this.userRepository.findAll().forEach(user -> {
            if (this.staffRepository.findByUser(user).isEmpty()) {
                this.departmentRepository.findByName("Admin & Human Resources").ifPresent(department -> {
                    Staff staff = new Staff(user, department, true);
                    this.staffRepository.save(staff);
                    log.info("Inserted staff: {} into department: {}", user.getUsername(), department.getName());
                });
            }
        });

        log.info("Staff initialization completed.");
    }
}
