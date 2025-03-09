package org.teamSmurfs.backend.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteUsageDto {
    private String routeName;
    private long visitCount;
}

