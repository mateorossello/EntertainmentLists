package dev.mateorossello.entertainmentlists.controllers;

import dev.mateorossello.entertainmentlists.dtos.EntertainmentListInput;
import dev.mateorossello.entertainmentlists.dtos.EntertainmentListOutput;
import dev.mateorossello.entertainmentlists.dtos.EntertainmentListOutputDetailed;
import dev.mateorossello.entertainmentlists.models.EntertainmentList;
import dev.mateorossello.entertainmentlists.services.EntertainmentListService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/entertainment-list")
public class EntertainmentListController {
    // Generic controller for entertainment lists

    private final EntertainmentListService entertainmentListService;

    public EntertainmentListController(EntertainmentListService entertainmentListService) {
        this.entertainmentListService = entertainmentListService;
    }

    @GetMapping("/{id}")
    public Mono<EntertainmentListOutputDetailed> getListById(@PathVariable Long id) {
        return entertainmentListService.getListById(id);
    }

    @GetMapping
    public List<EntertainmentListOutput> getAllLists() {
        return entertainmentListService.getAllLists();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> createList(@Valid @RequestBody EntertainmentListInput input) {
        EntertainmentList newList = entertainmentListService.createList(input);
        
        return Map.of("message", "List created successfully", "id", newList.getId().toString());
    }

    @PutMapping("/{id}")
    public Map<String, String> updateList(@PathVariable Long id, @Valid @RequestBody EntertainmentListInput input) {
        EntertainmentList updatedList = entertainmentListService.updateList(id, input);

        return Map.of("message", "List updated successfully", "id", updatedList.getId().toString());
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteList(@PathVariable Long id) {
        entertainmentListService.deleteList(id);

        return Map.of("message", "List deleted successfully");
    }
}
