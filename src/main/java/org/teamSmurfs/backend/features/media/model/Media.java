package org.teamSmurfs.backend.features.media.model;

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
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "uploaded_by", nullable = false)
    private User uploadedBy;
    
    @Column(nullable = false)
    private String userName;
    
    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @Column(nullable = false)
    private Long entityId;

    @Column(nullable = false)
    private Integer entityType;

    @Column(nullable = false)
    private String fileType;
    
    @Column(nullable = false)
	private String storedName;
	
    @Column(nullable = false)
	private String storedUUID;
	
    @Column(nullable = false)
	private String title;
	
    @Column(nullable = false)
	private String description;
	
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Media(
            final User uploadedBy,final String userName, final String fileUrl, final LocalDateTime uploadedAt, final Long entityId, final Integer entityType, 
            final String fileType,final String storedName,final String storedUUID, final String title, final String description ) {
        this.uploadedBy = uploadedBy;
        this.userName = userName;
        this.fileUrl = fileUrl;
        this.uploadedAt = uploadedAt;
        this.entityId = entityId;
        this.entityType = entityType;
        this.fileType = fileType;
        this.storedName = storedName;
        this.storedUUID = storedUUID;
        this.title = title;
        this.description = description;
        
    }
}
