package dev.mateorossello.entertainmentlists.controllers;

import dev.mateorossello.entertainmentlists.dtos.PaginatedResponse;
import dev.mateorossello.entertainmentlists.services.EntertainmentEntityService;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/entertainment-entity")
public class EntertainmentEntityController {
    // Generic controller for entertainment entities

    private final EntertainmentEntityService entertainmentEntityService;

    public EntertainmentEntityController(EntertainmentEntityService entertainmentEntityService) {
        this.entertainmentEntityService = entertainmentEntityService;
    }

    @GetMapping("/{id}")
    public Mono<Map<String, Object>> getEntityById(@PathVariable Long id, @RequestParam Map<String, String> parameters) {
        return entertainmentEntityService.getEntityById(id, parameters);
    }

    @GetMapping
    public Mono<PaginatedResponse> getAllEntities(@RequestParam Map<String, String> parameters) {
        return entertainmentEntityService.getAllEntities(parameters);
    }

    @GetMapping("/search")
    public Mono<PaginatedResponse> searchEntities(@RequestParam Map<String, String> parameters) {
        return entertainmentEntityService.searchEntities(parameters);
    }
}
