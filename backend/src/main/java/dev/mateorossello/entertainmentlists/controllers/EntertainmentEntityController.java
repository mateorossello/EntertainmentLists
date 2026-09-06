package dev.mateorossello.entertainmentlists.controllers;

import dev.mateorossello.entertainmentlists.dtos.PaginatedResponse;
import dev.mateorossello.entertainmentlists.services.registry.EntertainmentEntityServiceRegistry;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/entertainment-entity")
public class EntertainmentEntityController {
    // Generic controller for entertainment entities

    private final EntertainmentEntityServiceRegistry entertainmentEntityServiceRegistry;

    public EntertainmentEntityController(EntertainmentEntityServiceRegistry entertainmentEntityServiceRegistry) {
        this.entertainmentEntityServiceRegistry = entertainmentEntityServiceRegistry;
    }

    @GetMapping("/{id}")
    public Mono<Map<String, Object>> getEntityById(@PathVariable Long id, @RequestParam Map<String, String> parameters) {
        String provider = parameters.get("provider");
        return entertainmentEntityServiceRegistry.getService(provider).getEntityById(id, parameters);
    }

    @GetMapping
    public Mono<PaginatedResponse> getAllEntities(@RequestParam Map<String, String> parameters) {
        String provider = parameters.get("provider");
        return entertainmentEntityServiceRegistry.getService(provider).getAllEntities(parameters);
    }

    @GetMapping("/search")
    public Mono<PaginatedResponse> searchEntities(@RequestParam Map<String, String> parameters) {
        String provider = parameters.get("provider");
        return entertainmentEntityServiceRegistry.getService(provider).searchEntities(parameters);
    }
}
