package dev.mateorossello.entertainment_lists.Controllers;

import dev.mateorossello.entertainment_lists.DTOs.EntertainmentListInput;
import dev.mateorossello.entertainment_lists.DTOs.EntertainmentListOutput;
import dev.mateorossello.entertainment_lists.DTOs.EntertainmentListOutputDetailed;
import dev.mateorossello.entertainment_lists.Models.EntertainmentList;
import dev.mateorossello.entertainment_lists.Services.EntertainmentListService;
import java.util.List;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/entertainment-list")
public class EntertainmentListController {
    // Generic controller for entertainment lists
    private final EntertainmentListService entertainmentListService;

    public EntertainmentListController(EntertainmentListService entertainmentListService) {
        this.entertainmentListService = entertainmentListService;
    }

    @GetMapping
    public List<EntertainmentListOutput> getAllLists() {
        return entertainmentListService.getAllLists();
    }

    @GetMapping("/{id}")
    public EntertainmentListOutputDetailed getListById(@PathVariable Long id) {
        try {
            return entertainmentListService.getListById(id);
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(exception.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createList(@RequestBody EntertainmentListInput input, @RequestParam Map<String, String> parameters) {
        try {
            EntertainmentList newList = entertainmentListService.createList(input, parameters);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "List created successfully", "id", newList.getId().toString()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error creating list: " + exception.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateList(@PathVariable Long id, @RequestBody EntertainmentListInput input) {
        try {
            EntertainmentList updatedList = entertainmentListService.updateList(id, input);

            return ResponseEntity.ok(Map.of("message", "List updated successfully", "id", updatedList.getId().toString()));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error updating list: " + exception.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteList(@PathVariable Long id) {
        try {
            entertainmentListService.deleteList(id);

            return ResponseEntity.ok(Map.of("message", "List deleted successfully"));
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error deleting list: " + exception.getMessage()));
        }
    }
}
