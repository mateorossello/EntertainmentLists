package dev.mateorossello.entertainmentlists.dtos;

import java.util.List;
import java.util.Map;

public record PaginatedResponse (
    List<Map<String, Object>> data,
    boolean hasNextPage
) {
    public PaginatedResponse {
        data = List.copyOf(data);
    }
}
