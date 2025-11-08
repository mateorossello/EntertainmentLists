package dev.mateorossello.entertainment_lists.Services;

import dev.mateorossello.entertainment_lists.DTOs.PaginatedResponse;
import java.util.Map;

public interface EntertainmentEntityService {
    // Base service interface for entertainment entities
    // Each specific service for different types of entertainment entities will implement this interface
    // If your implementation does not use pagination, it will only return one page
    //
    // Warning: The getType() method is intended for a future multi-service factory implementation and is not consumed by the current controller
    //
    String getType();
    Map<String, Object> getEntityById(Long id, Map<String, String> parameters);
    PaginatedResponse getAllEntities(Map<String, String> parameters);
}
