package dev.mateorossello.entertainmentlists.services.tmdb;

import dev.mateorossello.entertainmentlists.dtos.PaginatedResponse;
import dev.mateorossello.entertainmentlists.services.EntertainmentEntityService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class EntertainmentEntityServiceTmdb implements EntertainmentEntityService {
     // Example of a specific service for TMDB entities
     // Supports types: movie, tv

    private static final String PROVIDER_NAME = "TMDB";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public EntertainmentEntityServiceTmdb(WebClient.Builder webClientBuilder, ObjectMapper objectMapper, @Value("${tmdb.api-key:}") String apiKey) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL)
            .defaultHeader("Authorization", "Bearer " + apiKey)
            .defaultHeader("Accept", "application/json")
            .build();
        this.objectMapper = objectMapper;
    }

    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }

    @Override
    public Mono<Map<String, Object>> getEntityById(Long id, Map<String, String> parameters) {
        String type = parameters.get("type");

        if (type == null) {
            return Mono.error(new IllegalArgumentException("Type parameter is required"));
        }

        return webClient.get()
            .uri(uriBuilder -> uriBuilder.path(type + "/" + id).build())
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    @Override
    public Mono<PaginatedResponse> getAllEntities(Map<String, String> parameters) {
        String type = parameters.get("type");

        if (type == null) {
            return Mono.error(new IllegalArgumentException("Type parameter is required"));
        }

        int page = extractPage(parameters);

        return webClient.get()
            .uri(uriBuilder -> {
                uriBuilder.path("discover/" + type)
                    .queryParam("page", page + 1);
                
                if (parameters.containsKey("sort")) {
                    uriBuilder.queryParam("sort_by", parameters.get("sort"));
                }
                
                return uriBuilder.build();
            })
            .retrieve()
            .bodyToMono(JsonNode.class)
            .map(this::createPaginatedResponse);
    }

    @Override
    public Mono<PaginatedResponse> searchEntities(Map<String, String> parameters) {
        String type = parameters.get("type");
        String query = parameters.get("query");

        if (type == null || query == null) {
            return Mono.error(new IllegalArgumentException("Type and query parameters are required"));
        }

        int page = extractPage(parameters);

        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("search/" + type)
                .queryParam("query", query)
                .queryParam("page", page + 1)
                .build())
            .retrieve()
            .bodyToMono(JsonNode.class)
            .map(this::createPaginatedResponse);
    }

    private int extractPage(Map<String, String> parameters) {
        return parameters.get("page") != null ? Integer.parseInt(parameters.get("page")) : 0;
    }

    private PaginatedResponse createPaginatedResponse(JsonNode jsonNode) {
        List<Map<String, Object>> data = new ArrayList<>();

        if (jsonNode.has("results") && jsonNode.get("results").isArray()) {
            for (JsonNode node : jsonNode.get("results")) {
                data.add(objectMapper.convertValue(node, new TypeReference<Map<String, Object>>() {}));
            }
        }

        int currentPage = jsonNode.path("page").asInt(1);
        int totalPages = jsonNode.path("total_pages").asInt(1);
        boolean hasNext = currentPage < totalPages;

        return new PaginatedResponse(data, hasNext);
    }
}
