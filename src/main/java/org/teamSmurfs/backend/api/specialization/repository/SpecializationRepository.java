package org.teamSmurfs.backend.api.specialization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.api.specialization.model.Specialization;

import java.util.List;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
    List<Specialization> findAllByOrderByCreatedAtDesc();
}
