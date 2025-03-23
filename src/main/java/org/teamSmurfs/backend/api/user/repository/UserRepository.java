/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2025
 * @Time : 11:40 PM
 */
package org.teamSmurfs.backend.api.user.repository;

import org.teamSmurfs.backend.api.user.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
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
            "WHERE r.name = :roleName ORDER BY u.created_at DESC", nativeQuery = true)
    List<User> findByRoleName(String roleName);
    
    Optional<User> findByUsername(String staff1);
    
    @EntityGraph(attributePaths = {"roles", "student", "staff", "tutor"})
    List<User> findAllByOrderByCreatedAtDesc();
    
    @EntityGraph(attributePaths = {"roles", "student", "staff", "tutor"})
    Optional<User> findById(Long id);

    @Query(value = """
        SELECT u.* FROM staff s
        LEFT JOIN user u ON u.id = s.user_id
        LEFT JOIN user_roles ur ON ur.user_id = u.id   
        LEFT JOIN role r ON r.id = ur.role_id
        WHERE s.is_admin = true AND r.name = 'ROLE_ADMIN'
        ORDER BY u.created_at DESC
    """, nativeQuery = true)
    List<User> findUsersWithAdminRole();

    @Query(value = """
            SELECT COUNT(*) FROM user u 
            WHERE YEAR(u.created_at) = YEAR(CURRENT_DATE()) 
            AND MONTH(u.created_at) = MONTH(CURRENT_DATE())
        """, nativeQuery = true)
        long thisMothIncreaseCnt();

}
