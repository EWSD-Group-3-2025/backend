package org.teamSmurfs.backend.features.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.teamSmurfs.backend.features.user.model.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private Integer categoryId;

    @Column(nullable = false)
    private String difficultyLevel;

    @Column(nullable = false)
    private Double rating;

    @Column(nullable = false)
    private String organizationName;

    @Column(nullable = false)
    private String organizationUrl;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long uploaderId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
