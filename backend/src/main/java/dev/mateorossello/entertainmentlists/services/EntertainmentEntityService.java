package dev.mateorossello.entertainmentlists.services;

import dev.mateorossello.entertainmentlists.dtos.PaginatedResponse;
import java.util.Map;
import reactor.core.publisher.Mono;

public interface EntertainmentEntityService {
    // Base service interface for entertainment entities
    // Each specific service for different types of entertainment entities will implement this interface
    // If your implementation does not use pagination, it will only return one page
    
    Mono<Map<String, Object>> getEntityById(Long id, Map<String, String> parameters);

    Mono<PaginatedResponse> getAllEntities(Map<String, String> parameters);
    
    Mono<PaginatedResponse> searchEntities(Map<String, String> parameters);
}
