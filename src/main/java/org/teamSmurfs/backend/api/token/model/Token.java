/*
 * @Author : Kyaw Thuya
 * @Date : 05/02/2025
 * @Time : 10:00 PM
 */
package org.teamSmurfs.backend.api.token.model;

import java.time.Instant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private Long id;

@Column(name="user_id", nullable=false, unique=true)
private Long userId;

@Column(nullable=false, unique=true)
private String refreshtoken;

@Column(nullable=false)
private Instant expiredAt;
}
