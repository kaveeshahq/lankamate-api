package lk.lankamate.api.budget;

import lk.lankamate.api.budget.dto.BudgetDtos.BudgetSummary;
import lk.lankamate.api.budget.dto.BudgetDtos.CategoryBreakdown;
import lk.lankamate.api.budget.dto.BudgetDtos.DayBreakdown;
import lk.lankamate.api.common.exception.ResourceNotFoundException;
import lk.lankamate.api.itinerary.Activity;
import lk.lankamate.api.itinerary.DayPlan;
import lk.lankamate.api.itinerary.Itinerary;
import lk.lankamate.api.itinerary.ItineraryRepository;
import lk.lankamate.api.trip.Trip;
import lk.lankamate.api.trip.TripRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BudgetService {

    private final TripRepository tripRepository;
    private final ItineraryRepository itineraryRepository;

    public BudgetService(TripRepository tripRepository,
                         ItineraryRepository itineraryRepository) {
        this.tripRepository = tripRepository;
        this.itineraryRepository = itineraryRepository;
    }

    @Transactional(readOnly = true)
    public BudgetSummary getBudget(UUID tripId, UUID ownerId) {
        Trip trip = tripRepository.findByIdAndUserId(tripId, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", tripId));

        Itinerary itinerary = itineraryRepository.findByTripId(tripId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No itinerary yet — generate one to see the budget"));

        BigDecimal estimatedTotal = BigDecimal.ZERO;
        int activityCount = 0;

        Map<String, BigDecimal> categoryAmount = new LinkedHashMap<>();
        Map<String, Integer> categoryCount = new LinkedHashMap<>();
        List<DayBreakdown> byDay = new ArrayList<>();

        for (DayPlan day : itinerary.getDayPlans()) {
            BigDecimal dayTotal = BigDecimal.ZERO;

            for (Activity a : day.getActivities()) {
                BigDecimal cost = a.getEstimatedCost() != null
                        ? a.getEstimatedCost() : BigDecimal.ZERO;
                estimatedTotal = estimatedTotal.add(cost);
                dayTotal = dayTotal.add(cost);
                activityCount++;

                String cat = a.getCategory() != null
                        ? a.getCategory().name() : "OTHER";
                categoryAmount.merge(cat, cost, BigDecimal::add);
                categoryCount.merge(cat, 1, Integer::sum);
            }

            byDay.add(new DayBreakdown(day.getDayNumber(), day.getTitle(), dayTotal));
        }

        List<CategoryBreakdown> byCategory = new ArrayList<>();
        categoryAmount.forEach((cat, amount) ->
                byCategory.add(new CategoryBreakdown(
                        cat, amount, categoryCount.getOrDefault(cat, 0))));
        byCategory.sort((a, b) -> b.amount().compareTo(a.amount()));

        BigDecimal budget = trip.getTotalBudget();
        BigDecimal remaining = budget != null
                ? budget.subtract(estimatedTotal) : null;

        return new BudgetSummary(
                budget,
                trip.getCurrency(),
                estimatedTotal,
                remaining,
                activityCount,
                byCategory,
                byDay
        );
    }
}