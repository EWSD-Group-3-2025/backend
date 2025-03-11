package org.teamSmurfs.backend.api.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.teamSmurfs.backend.api.user.model.Tutor;
import org.teamSmurfs.backend.api.user.model.User;

import java.util.List;
import java.util.Optional;

public interface TutorRepository extends JpaRepository<Tutor, Long> {
    Optional<Tutor> findByUser(User user);

    List<Tutor> findBySpecializationId(Long id);

    @Query("SELECT COUNT(t) FROM Tutor t WHERE t.user.status = true")
    long countActiveTutors();

    Optional<Tutor> findByUserId(Long userId);

}
