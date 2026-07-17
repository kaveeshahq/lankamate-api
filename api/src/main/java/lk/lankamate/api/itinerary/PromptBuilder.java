package lk.lankamate.api.itinerary;

import lk.lankamate.api.trip.Trip;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Component
public class PromptBuilder {

    public String buildItineraryPrompt(Trip trip) {
        long days = ChronoUnit.DAYS.between(trip.getStartDate(), trip.getEndDate()) + 1;

        String interests = trip.getInterests().isEmpty()
                ? "general sightseeing"
                : trip.getInterests().stream()
                .map(Enum::name)
                .map(String::toLowerCase)
                .collect(Collectors.joining(", "));

        String budgetLine = trip.getTotalBudget() != null
                ? "Total budget: %s %s for the whole trip.".formatted(
                trip.getTotalBudget(), trip.getCurrency())
                : "Budget: flexible.";

        String base = trip.getBaseLocationName() != null
                ? trip.getBaseLocationName()
                : "Sri Lanka (traveller has not fixed a base city)";

        return """
                You are an expert Sri Lankan travel guide with deep local knowledge.
                Create a detailed %d-day travel itinerary for a tourist visiting Sri Lanka.

                TRIP CONTEXT
                - Trip title: %s
                - Duration: %d days (from %s to %s)
                - Base location: %s
                - Travel style: %s
                - Interests: %s
                - %s

                REQUIREMENTS
                - Plan realistic days: group nearby places, account for travel time,
                  and respect Sri Lankan opening hours and culture.
                - Each day must have 3 to 5 activities spread across MORNING, AFTERNOON, EVENING.
                - Give practical, specific places (real attractions, restaurants, areas).
                - Keep the plan within the stated budget and travel style.
                - Add a short useful tip per activity where helpful.
                - Estimate a per-activity cost in %s (a number, no currency symbol).

                OUTPUT FORMAT — VERY IMPORTANT
                Respond with ONLY valid minified JSON, no markdown, no commentary,
                no code fences. Use exactly this structure:
                {
                  "overview": "2-3 sentence summary of the whole trip",
                  "days": [
                    {
                      "dayNumber": 1,
                      "title": "short day theme",
                      "summary": "1-2 sentence summary of the day",
                      "activities": [
                        {
                          "title": "activity name",
                          "description": "what to do and why",
                          "timeOfDay": "MORNING",
                          "category": "SIGHTSEEING",
                          "locationName": "place name",
                          "latitude": 6.9271,
                          "longitude": 79.8612,
                          "estimatedCost": 15,
                          "tip": "a short practical tip"
                        }
                      ]
                    }
                  ]
                }

                Allowed timeOfDay values: MORNING, AFTERNOON, EVENING.
                Allowed category values: SIGHTSEEING, FOOD, ADVENTURE, RELAXATION,
                CULTURAL, NATURE, SHOPPING, TRANSPORT, ACCOMMODATION, OTHER.
                If you don't know exact coordinates, use your best estimate for the area.
                Return JSON for all %d days.
                """.formatted(
                days,
                trip.getTitle(),
                days, trip.getStartDate(), trip.getEndDate(),
                base,
                trip.getTravelStyle().name(),
                interests,
                budgetLine,
                trip.getCurrency(),
                days
        );
    }
}