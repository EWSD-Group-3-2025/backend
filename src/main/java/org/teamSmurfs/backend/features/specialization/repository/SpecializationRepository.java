package org.teamSmurfs.backend.features.specialization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.features.specialization.model.Specialization;

import java.util.List;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {
    List<Specialization> findAllByOrderByCreatedAtDesc();

	boolean existsByName(String specializationName);
}
