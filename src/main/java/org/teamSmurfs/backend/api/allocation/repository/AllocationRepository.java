package org.teamSmurfs.backend.api.allocation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.api.allocation.model.Allocation;

public interface AllocationRepository extends JpaRepository<Allocation, Long> {
}
