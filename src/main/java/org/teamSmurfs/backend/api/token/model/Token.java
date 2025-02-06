package org.teamSmurfs.backend.api.token.model;

import java.time.Instant;

import org.teamSmurfs.backend.api.user.model.User;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String refreshtoken;

    @Column(nullable = false)
    private Instant expiredAt;
    
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
