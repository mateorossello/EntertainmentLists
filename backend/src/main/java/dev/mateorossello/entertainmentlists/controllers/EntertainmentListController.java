package dev.mateorossello.entertainmentlists.controllers;

import dev.mateorossello.entertainmentlists.dtos.EntertainmentListInput;
import dev.mateorossello.entertainmentlists.dtos.EntertainmentListOutput;
import dev.mateorossello.entertainmentlists.dtos.EntertainmentListOutputDetailed;
import dev.mateorossello.entertainmentlists.dtos.SuccessResponse;
import dev.mateorossello.entertainmentlists.models.EntertainmentList;
import dev.mateorossello.entertainmentlists.services.EntertainmentListService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
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
    public List<EntertainmentListOutput> getAllLists(Principal principal) {
        return entertainmentListService.getAllListsForUser(principal.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse createList(@Valid @RequestBody EntertainmentListInput input, Principal principal) {
        EntertainmentList newList = entertainmentListService.createList(input, principal.getName());
        
        return new SuccessResponse("List created successfully", newList.getId().toString());
    }

    @PutMapping("/{id}")
    public SuccessResponse updateList(@PathVariable Long id, @Valid @RequestBody EntertainmentListInput input, Principal principal) {
        entertainmentListService.updateList(id, input, principal.getName());

        return new SuccessResponse("List updated successfully");
    }

    @DeleteMapping("/{id}")
    public SuccessResponse deleteList(@PathVariable Long id, Principal principal) {
        entertainmentListService.deleteList(id, principal.getName());

        return new SuccessResponse("List deleted successfully");
    }

    @PostMapping("/{id}/entities/{entityId}")
    public SuccessResponse addEntityToList(@PathVariable Long id, @PathVariable Long entityId, Principal principal) {
        entertainmentListService.addEntityToList(id, entityId, principal.getName());
        
        return new SuccessResponse("Entity added successfully to the list");
    }

    @DeleteMapping("/{id}/entities/{entityId}")
    public SuccessResponse removeEntityFromList(@PathVariable Long id, @PathVariable Long entityId, Principal principal) {
        entertainmentListService.removeEntityFromList(id, entityId, principal.getName());
        
        return new SuccessResponse("Entity removed successfully from the list");
    }
}
