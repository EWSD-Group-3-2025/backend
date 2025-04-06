package org.teamSmurfs.backend.features.visit_log.model;

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
public class VisitLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    select created_at from visit_log where user_id = 8 order by id desc limit 1;

    today = feb 28

    1. Login              Monday (jan31)
    2. Dashboard          Tuesday
    3. Dashboard          Wednesday
    4. Students           Wednesday
    5. Logout             Sunday (feb 1)

    inactive_day_count = today - last_row_of_created_at
                       =  feb 28 - feb 1
                       = 28

    last_record_created_date = 5 - created at feb 1

    today = feb 28

    boolean checkValidMailSendDay( today, last_record_created_date) {

	    return (today - last_record_created_date == 28);

    }

    feb 2 - feb 2 = 0 (false)
    feb 28 - feb 1 = 28 (true) -> mail

    */

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = true)
    private String routeName;

    @Column(nullable = true)
    private String browserName;

    @Column(nullable = true)
    private String pageName;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public VisitLog(final User user, final String routeName, final String browserName, final String pageName) {
        this.user = user;
        this.routeName = routeName;
        this.browserName = browserName;
        this.pageName = pageName;
    }


}
