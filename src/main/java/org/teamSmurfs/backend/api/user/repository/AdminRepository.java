package org.teamSmurfs.backend.api.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.teamSmurfs.backend.api.user.model.Admin;
import org.teamSmurfs.backend.api.user.model.User;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

}
