/*
 * @Author : Thant Htoo Aung
 * @Date : 2/3/2025
 * @Time : 09:50 AM (UTC)
 */
package org.teamSmurfs.backend.api.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.teamSmurfs.backend.api.role.model.Role;
import org.teamSmurfs.backend.api.role.model.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
