package lk.lankamate.api.trip;

import lk.lankamate.api.trip.dto.TripResponse;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.HashSet;

@Component
public class TripMapper {

    public TripResponse toResponse(Trip trip) {
        int durationDays = (int) ChronoUnit.DAYS.between(
                trip.getStartDate(), trip.getEndDate()) + 1;

        return new TripResponse(
                trip.getId(),
                trip.getTitle(),
                trip.getDescription(),
                trip.getStartDate(),
                trip.getEndDate(),
                durationDays,
                trip.getBaseLocationName(),
                trip.getBaseLatitude(),
                trip.getBaseLongitude(),
                trip.getTotalBudget(),
                trip.getCurrency(),
                trip.getTravelStyle(),
                trip.getStatus(),
                new HashSet<>(trip.getInterests()),
                trip.getCreatedAt(),
                trip.getUpdatedAt()
        );
    }
}