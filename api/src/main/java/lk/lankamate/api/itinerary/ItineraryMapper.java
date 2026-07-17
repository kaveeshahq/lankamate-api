package lk.lankamate.api.itinerary;

import lk.lankamate.api.itinerary.dto.ItineraryResponseDtos.ActivityResponse;
import lk.lankamate.api.itinerary.dto.ItineraryResponseDtos.DayResponse;
import lk.lankamate.api.itinerary.dto.ItineraryResponseDtos.ItineraryResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItineraryMapper {

    public ItineraryResponse toResponse(Itinerary itinerary) {
        List<DayResponse> days = itinerary.getDayPlans().stream()
                .map(this::toDayResponse)
                .toList();

        return new ItineraryResponse(
                itinerary.getId(),
                itinerary.getTrip().getId(),
                itinerary.getOverview(),
                itinerary.getGeneratedBy(),
                days
        );
    }

    private DayResponse toDayResponse(DayPlan day) {
        List<ActivityResponse> activities = day.getActivities().stream()
                .map(this::toActivityResponse)
                .toList();

        return new DayResponse(
                day.getId(),
                day.getDayNumber(),
                day.getDate(),
                day.getTitle(),
                day.getSummary(),
                activities
        );
    }

    private ActivityResponse toActivityResponse(Activity a) {
        return new ActivityResponse(
                a.getId(),
                a.getTitle(),
                a.getDescription(),
                a.getTimeOfDay() != null ? a.getTimeOfDay().name() : null,
                a.getCategory() != null ? a.getCategory().name() : null,
                a.getOrderIndex(),
                a.getLocationName(),
                a.getLatitude(),
                a.getLongitude(),
                a.getEstimatedCost(),
                a.getTip()
        );
    }
}