package dev.mateorossello.entertainment_lists.Services;

import java.util.Map;

public interface EntertainmentEntityService {
    // Base service interface for entertainment entities
    // Each specific service for different types of entertainment entities will implement this interface
    Map<String, Object> getEntityById(Long id, Map<String, String> parameters);
}
