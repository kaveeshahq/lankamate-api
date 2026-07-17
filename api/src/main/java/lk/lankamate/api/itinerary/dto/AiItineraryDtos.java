package lk.lankamate.api.itinerary.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public final class AiItineraryDtos {

    private AiItineraryDtos() {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AiItinerary(
            String overview,
            List<AiDay> days
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AiDay(
            int dayNumber,
            String title,
            String summary,
            List<AiActivity> activities
    ) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AiActivity(
            String title,
            String description,
            String timeOfDay,
            String category,
            String locationName,
            Double latitude,
            Double longitude,
            Double estimatedCost,
            String tip
    ) {
    }
}