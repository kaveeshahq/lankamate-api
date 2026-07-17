package lk.lankamate.api.trip.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lk.lankamate.api.trip.Interest;
import lk.lankamate.api.trip.TravelStyle;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record CreateTripRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 120, message = "Title must be at most 120 characters")
        String title,

        @Size(max = 1000, message = "Description must be at most 1000 characters")
        String description,

        @NotNull(message = "Start date is required")
        @FutureOrPresent(message = "Start date cannot be in the past")
        LocalDate startDate,

        @NotNull(message = "End date is required")
        LocalDate endDate,

        String baseLocationName,
        Double baseLatitude,
        Double baseLongitude,

        @Positive(message = "Budget must be positive")
        BigDecimal totalBudget,

        String currency,

        TravelStyle travelStyle,

        Set<Interest> interests
) {
}