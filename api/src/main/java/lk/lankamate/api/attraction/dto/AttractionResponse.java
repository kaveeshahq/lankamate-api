package lk.lankamate.api.attraction.dto;

import java.util.UUID;

public record AttractionResponse(
        UUID id,
        String name,
        String description,
        String type,
        String region,
        Double latitude,
        Double longitude,
        Double rating,
        Double entryFee,
        String bestTime,
        String tip,
        String imageUrl
) {
}