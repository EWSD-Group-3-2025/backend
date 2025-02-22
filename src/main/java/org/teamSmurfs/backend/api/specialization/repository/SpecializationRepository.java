package org.teamSmurfs.backend.api.specialization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.api.specialization.model.Specialization;

public interface SpecializationRepository extends JpaRepository<Specialization, Long> {

}
