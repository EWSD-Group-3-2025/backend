package org.teamSmurfs.backend.api.visit_log.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitLogDto {
    private String routeName;

    private String browserName;

    private String username;

    private String pageName;
}
