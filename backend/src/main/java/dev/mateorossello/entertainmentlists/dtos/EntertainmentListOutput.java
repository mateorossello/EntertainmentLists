package dev.mateorossello.entertainmentlists.dtos;

public record EntertainmentListOutput(
    Long id,
    String name,
    String provider,
    String type
) {}
