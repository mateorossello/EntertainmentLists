package dev.mateorossello.entertainmentlists.services.registry;

import dev.mateorossello.entertainmentlists.services.EntertainmentEntityService;
import java.util.function.Function;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class EntertainmentEntityServiceRegistry {
    private final Map<String, EntertainmentEntityService> services;

    public EntertainmentEntityServiceRegistry(List<EntertainmentEntityService> serviceList) {
        this.services = serviceList.stream().collect(Collectors.toMap(EntertainmentEntityService::getProviderName, Function.identity()));
    }

    public EntertainmentEntityService getService(String provider) {
        if (provider == null || provider.isBlank()) {
            throw new IllegalArgumentException("Provider parameter is required");
        }
        
        EntertainmentEntityService service = services.get(provider.toUpperCase());

        if (service == null) {
            throw new IllegalArgumentException("Unknown provider: " + provider);
        }
        
        return service;
    }
}
