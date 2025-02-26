package org.teamSmurfs.backend.api.token.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teamSmurfs.backend.api.token.model.Token;
import org.teamSmurfs.backend.api.user.model.User;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUserAndExpiredAtAfter(User user, Instant currentTime);
}
