package dev.mateorossello.entertainmentlists.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record EntertainmentListInput(
    @NotBlank(message = "Name cannot be empty")
    String name,

    @NotBlank(message = "Provider cannot be empty")
    String provider,
    
    String type,
    
    @NotNull(message = "Entity IDs list cannot be null")
    List<Long> entertainmentEntityIds
) {}
