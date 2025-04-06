package org.teamSmurfs.backend.features.token.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teamSmurfs.backend.features.token.model.Token;
import org.teamSmurfs.backend.features.user.model.User;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUserAndExpiredAtAfter(User user, Instant currentTime);
}
