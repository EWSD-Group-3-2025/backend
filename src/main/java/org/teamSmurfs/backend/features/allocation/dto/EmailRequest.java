package org.teamSmurfs.backend.features.allocation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest implements Serializable {
    private String recipientEmail;
    private String recipientType;
    private String tutorName;
    private String studentName;
}
