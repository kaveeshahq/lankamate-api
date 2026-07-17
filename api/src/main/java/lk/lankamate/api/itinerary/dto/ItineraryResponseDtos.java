package lk.lankamate.api.itinerary.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public final class ItineraryResponseDtos {

    private ItineraryResponseDtos() {
    }

    public record ItineraryResponse(
            UUID id,
            UUID tripId,
            String overview,
            String generatedBy,
            List<DayResponse> days
    ) {
    }

    public record DayResponse(
            UUID id,
            int dayNumber,
            LocalDate date,
            String title,
            String summary,
            List<ActivityResponse> activities
    ) {
    }

    public record ActivityResponse(
            UUID id,
            String title,
            String description,
            String timeOfDay,
            String category,
            int orderIndex,
            String locationName,
            Double latitude,
            Double longitude,
            BigDecimal estimatedCost,
            String tip
    ) {
    }
}