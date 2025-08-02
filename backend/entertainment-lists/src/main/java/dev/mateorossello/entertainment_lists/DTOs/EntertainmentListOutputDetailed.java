package dev.mateorossello.entertainment_lists.DTOs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntertainmentListOutputDetailed {
    private Long id;
    private String name;
    private List<Map<String, Object>> metadata = new ArrayList<>();

    public void addEntertainmentEntity(Map<String, Object> entity) {
        metadata.add(entity);
    }
}
