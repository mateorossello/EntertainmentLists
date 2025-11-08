package dev.mateorossello.entertainment_lists.DTOs;

import java.util.List;
import java.util.Map;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse {
    private List<Map<String, Object>> data;
    private boolean hasNextPage;
}
