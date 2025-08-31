package dev.mateorossello.entertainment_lists.Services;

import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//
// EXAMPLE SERVICE. CAN BE USED AS A TEMPLATE FOR OTHER SERVICES OR DELETE IF NOT NEEDED
//

@Service
public class EntertainmentEntityServiceKitsu implements EntertainmentEntityService {
    // Example of a specific service for Kitsu entities
    private static final String BASE_URL = "https://kitsu.io/api/edge/";
    private final RestTemplate restTemplate;

    public EntertainmentEntityServiceKitsu(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    @Override
    public Map<String, Object> getEntityById(Long id, Map<String, String> parameters) {
        String type = parameters.get("type");
        if (type == null) {
            throw new IllegalArgumentException("Type parameter is required.");
        }

        String url = BASE_URL + type + "/" + id;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.api+json");
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<Map<String, Object>>() {});
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch entity: " + response.getStatusCode() + ".");
        }
    }
}
