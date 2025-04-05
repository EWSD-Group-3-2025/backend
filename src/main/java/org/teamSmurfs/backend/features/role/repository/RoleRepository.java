/*
 * @Author : Thant Htoo Aung
 * @Date : 2/3/2025
 * @Time : 09:50 AM (UTC)
 */
package org.teamSmurfs.backend.features.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.teamSmurfs.backend.features.role.model.Role;
import org.teamSmurfs.backend.features.role.model.RoleName;

import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);

    @Query("SELECT r FROM Role r JOIN User u ON u.id = :userId")
    Set<Role> findRolesByUserId(@Param("userId") Long userId);
}
