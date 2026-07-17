package lk.lankamate.api.trip.dto;

import lk.lankamate.api.trip.Interest;
import lk.lankamate.api.trip.TravelStyle;
import lk.lankamate.api.trip.TripStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record TripResponse(
        UUID id,
        String title,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        int durationDays,
        String baseLocationName,
        Double baseLatitude,
        Double baseLongitude,
        BigDecimal totalBudget,
        String currency,
        TravelStyle travelStyle,
        TripStatus status,
        Set<Interest> interests,
        Instant createdAt,
        Instant updatedAt
) {
}