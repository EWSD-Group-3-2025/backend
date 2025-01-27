/*
 * @Author : Thant Htoo Aung
 * @Date : 1/15/2025
 * @Time : 09:31 PM
 */
package org.teamSmurfs.backend.api.response.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginatedResponse<T> {
    private List<T> items;
    private long totalItems;
    private int lastPage;
}
