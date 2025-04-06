package org.teamSmurfs.backend.features.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteUsageDto {
    private String pageName;
    private long visitCount;
}

