package org.teamSmurfs.backend.api.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.teamSmurfs.backend.api.department.model.Department;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "staff")
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "is_admin", nullable = false)
    private boolean admin;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    public Staff(final User user, final Department department, final boolean admin) {
        this.user = user;
        this.department = department;
        this.admin = admin;
    }
}
