/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2025
 * @Time : 11:40 PM
 */
package org.teamSmurfs.backend.api.user.repository;


import org.teamSmurfs.backend.api.role.model.RoleName;
import org.teamSmurfs.backend.api.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	@Query(value = "SELECT * FROM user u LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<User> findUsersWithPagination(@Param("offset") int offset, @Param("limit") int limit);

    @Query("SELECT COUNT(u) FROM User u")
    long countUsers();

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String userEmail);

    boolean existsByUsername(String username);

    @Query(value = "SELECT COUNT(*) FROM user WHERE username = :username", nativeQuery = true)
    int countByUsername(@Param("username") String username);

    @Query(value = "SELECT COUNT(*) FROM user WHERE name = :name", nativeQuery = true)
    int countByName(@Param("name") String username);

    @Query(value = "SELECT u.* FROM user u " +
            "JOIN user_roles ur ON u.id = ur.user_id " +
            "JOIN role r ON ur.role_id = r.id " +
            "WHERE r.name = :roleName", nativeQuery = true)
    List<User> findByRoleName(String roleName);

    List<User> findByUsername(String staff1);
}
