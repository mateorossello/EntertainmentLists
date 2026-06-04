package dev.mateorossello.entertainmentlists.services.kitsu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mateorossello.entertainmentlists.dtos.PaginatedResponse;
import dev.mateorossello.entertainmentlists.services.EntertainmentEntityService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class EntertainmentEntityServiceKitsu implements EntertainmentEntityService {
    // Example of a specific service for Kitsu entities

    private static final String BASE_URL = "https://kitsu.io/api/edge/";
    private static final int LIMIT = 10;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public EntertainmentEntityServiceKitsu(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Map<String, Object>> getEntityById(Long id, Map<String, String> parameters) {
        String type = parameters.get("type");

        if (type == null) {
            return Mono.error(new IllegalArgumentException("Type parameter is required"));
        }

        return webClient.get()
            .uri(uriBuilder -> uriBuilder
            .path(type + "/" + id)
            .build())
            .header("Accept", "application/vnd.api+json")
            .retrieve()
            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
    }

    @Override
    public Mono<PaginatedResponse> getAllEntities(Map<String, String> parameters) {
        String type = parameters.get("type");

        if (type == null) {
            return Mono.error(new IllegalArgumentException("Type parameter is required"));
        }

        int page = extractPage(parameters, 0);
        int offset = extractOffset(page, LIMIT);

        return webClient.get()
            .uri(uriBuilder -> {
                uriBuilder.path(type)
                    .queryParam("page[limit]", LIMIT)
                    .queryParam("page[offset]", offset);
                
                if (parameters.containsKey("sort")) {
                    uriBuilder.queryParam("sort", parameters.get("sort"));
                }
                
                return uriBuilder.build();
            })
            .header("Accept", "application/vnd.api+json")
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

        // Kitsu API bug (Temporary fix)
        // Search pagination is apparently broken after testing it
        // This search by parameters implementation will only return the first page

        return webClient.get()
            .uri(uriBuilder -> {
                uriBuilder.path(type)
                    .queryParam("page[limit]", 20)
                    .queryParam("page[offset]", 0)
                    .queryParam("filter[text]", query);

                if (parameters.containsKey("sort")) {
                    uriBuilder.queryParam("sort", parameters.get("sort"));
                }
                
                return uriBuilder.build();
            })
            .header("Accept", "application/vnd.api+json")
            .retrieve()
            .bodyToMono(JsonNode.class)
            .map(node -> {
                PaginatedResponse response = createPaginatedResponse(node);
                // Force hasNext to false and disable Next button in frontend
                return new PaginatedResponse(response.data(), false);
            });
    }

    private int extractPage(Map<String, String> parameters, int defaultPage) {
        return parameters.get("page") != null ? Integer.parseInt(parameters.get("page")) : defaultPage;
    }

    private int extractOffset(int page, int limit) {
        return page * limit;
    }

    private PaginatedResponse createPaginatedResponse(JsonNode jsonNode) {
        List<Map<String, Object>> data = new ArrayList<>();
        
        if (jsonNode.has("data") && jsonNode.get("data").isArray()) {
            for (JsonNode node : jsonNode.get("data")) {
                data.add(objectMapper.convertValue(node, new TypeReference<Map<String, Object>>() {}));
            }
        }
        
        boolean hasNext = (jsonNode.has("links") && jsonNode.get("links").has("next"));
        
        return new PaginatedResponse(data, hasNext);
    }
}
