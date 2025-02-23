package org.teamSmurfs.backend.api.react.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.api.react.model.React;

public interface ReactRepository extends JpaRepository<React, Long> {
}
