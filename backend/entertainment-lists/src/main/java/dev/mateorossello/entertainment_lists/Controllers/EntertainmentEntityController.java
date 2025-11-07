package dev.mateorossello.entertainment_lists.Controllers;

import dev.mateorossello.entertainment_lists.Services.EntertainmentEntityService;
import java.util.List;
import java.util.Map;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/entertainment-entity")
public class EntertainmentEntityController {
    private final EntertainmentEntityService entertainmentEntityService;

    public EntertainmentEntityController(EntertainmentEntityService entertainmentEntityService) {
        this.entertainmentEntityService = entertainmentEntityService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEntityById(@PathVariable Long id, @RequestParam Map<String, String> parameters) {
        try {
            Map<String, Object> entity = entertainmentEntityService.getEntityById(id, parameters);
            
            return ResponseEntity.ok(entity);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching entity: " + exception.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllEntities(@RequestParam Map<String, String> parameters) {
        try {
            List<Map<String, Object>> entities = entertainmentEntityService.getAllEntities(parameters);
            
            return ResponseEntity.ok(entities);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error fetching entity: " + exception.getMessage()));
        }
    }
}
