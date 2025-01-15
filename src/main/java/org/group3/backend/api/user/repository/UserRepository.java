/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2024
 * @Time : 11:40 PM
 */
package org.group3.backend.api.user.repository;

import org.group3.backend.api.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u")
    List<User> findUsersWithPagination(@Param("offset") int offset, @Param("limit") int limit);

    @Query("SELECT COUNT(u) FROM User u")
    long countUsers();
}
