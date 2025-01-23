/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2025
 * @Time : 11:35 PM
 */
package org.group3.backend.api.user.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
}
