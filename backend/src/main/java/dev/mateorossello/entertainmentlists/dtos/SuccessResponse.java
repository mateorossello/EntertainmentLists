package dev.mateorossello.entertainmentlists.dtos;

public record SuccessResponse(
    String message,

    String id
) {
    public SuccessResponse(String message) {
        this(message, null);
    }
}
