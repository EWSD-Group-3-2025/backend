/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2025
 * @Time : 11:44 PM
 */
package org.teamSmurfs.backend.api.role.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleName name;
}
