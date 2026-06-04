package dev.mateorossello.entertainmentlists.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record EntertainmentListOutputDetailed (
    Long id,
    String provider,
    String type,
    String name,
    List<Map<String, Object>> metadata
) {
    public EntertainmentListOutputDetailed {
        metadata = new ArrayList<>(metadata);
    }

    public void addEntertainmentEntity(Map<String, Object> entity) {
        metadata.add(entity);
    }
}
