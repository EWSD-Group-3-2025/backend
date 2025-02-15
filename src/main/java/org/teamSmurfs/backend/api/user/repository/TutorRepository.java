package org.teamSmurfs.backend.api.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.api.user.model.Tutor;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
}
