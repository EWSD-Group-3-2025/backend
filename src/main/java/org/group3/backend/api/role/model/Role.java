/*
 * @Author : Thant Htoo Aung
 * @Date : 1/14/2025
 * @Time : 11:44 PM
 */
package org.group3.backend.api.role.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
}
