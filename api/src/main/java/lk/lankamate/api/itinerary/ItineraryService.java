package lk.lankamate.api.itinerary;

import com.fasterxml.jackson.databind.ObjectMapper;
import lk.lankamate.api.ai.provider.AiProvider;
import lk.lankamate.api.common.exception.BadRequestException;
import lk.lankamate.api.common.exception.ResourceNotFoundException;
import lk.lankamate.api.itinerary.dto.AiItineraryDtos.AiActivity;
import lk.lankamate.api.itinerary.dto.AiItineraryDtos.AiDay;
import lk.lankamate.api.itinerary.dto.AiItineraryDtos.AiItinerary;
import lk.lankamate.api.itinerary.dto.ItineraryResponseDtos.ItineraryResponse;
import lk.lankamate.api.trip.Trip;
import lk.lankamate.api.trip.TripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class ItineraryService {

    private static final Logger log = LoggerFactory.getLogger(ItineraryService.class);

    private final TripRepository tripRepository;
    private final ItineraryRepository itineraryRepository;
    private final AiProvider aiProvider;
    private final PromptBuilder promptBuilder;
    private final ItineraryMapper itineraryMapper;
    private final ObjectMapper objectMapper;

    public ItineraryService(TripRepository tripRepository,
                            ItineraryRepository itineraryRepository,
                            AiProvider aiProvider,
                            PromptBuilder promptBuilder,
                            ItineraryMapper itineraryMapper,
                            ObjectMapper objectMapper) {
        this.tripRepository = tripRepository;
        this.itineraryRepository = itineraryRepository;
        this.aiProvider = aiProvider;
        this.promptBuilder = promptBuilder;
        this.itineraryMapper = itineraryMapper;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public ItineraryResponse generate(UUID tripId, UUID ownerId) {
        Trip trip = tripRepository.findByIdAndUserId(tripId, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", tripId));

        String prompt = promptBuilder.buildItineraryPrompt(trip);

        String raw = aiProvider.generateText(prompt);

        AiItinerary ai = parseAiResponse(raw);

        itineraryRepository.findByTripId(tripId)
                .ifPresent(itineraryRepository::delete);
        itineraryRepository.flush();

        Itinerary itinerary = Itinerary.builder()
                .trip(trip)
                .overview(ai.overview())
                .generatedBy(aiProvider.name())
                .build();

        if (ai.days() != null) {
            for (AiDay aiDay : ai.days()) {
                DayPlan day = DayPlan.builder()
                        .dayNumber(aiDay.dayNumber())
                        .title(aiDay.title())
                        .summary(aiDay.summary())
                        .date(trip.getStartDate() != null
                                ? trip.getStartDate().plusDays(
                                Math.max(0, aiDay.dayNumber() - 1))
                                : null)
                        .build();

                int order = 0;
                if (aiDay.activities() != null) {
                    for (AiActivity aiAct : aiDay.activities()) {
                        Activity activity = Activity.builder()
                                .title(aiAct.title())
                                .description(aiAct.description())
                                .timeOfDay(parseTimeOfDay(aiAct.timeOfDay()))
                                .category(parseCategory(aiAct.category()))
                                .orderIndex(order++)
                                .locationName(aiAct.locationName())
                                .latitude(aiAct.latitude())
                                .longitude(aiAct.longitude())
                                .estimatedCost(aiAct.estimatedCost() != null
                                        ? BigDecimal.valueOf(aiAct.estimatedCost())
                                        : null)
                                .tip(aiAct.tip())
                                .build();
                        day.addActivity(activity);
                    }
                }
                itinerary.addDayPlan(day);
            }
        }

        Itinerary saved = itineraryRepository.save(itinerary);
        return itineraryMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ItineraryResponse getForTrip(UUID tripId, UUID ownerId) {
        if (!tripRepository.existsByIdAndUserId(tripId, ownerId)) {
            throw new ResourceNotFoundException("Trip", tripId);
        }
        Itinerary itinerary = itineraryRepository.findByTripId(tripId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No itinerary found for this trip yet"));
        return itineraryMapper.toResponse(itinerary);
    }

    private AiItinerary parseAiResponse(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new BadRequestException("AI returned an empty itinerary");
        }

        String json = extractJson(raw);
        try {
            return objectMapper.readValue(json, AiItinerary.class);
        } catch (Exception e) {
            log.error("Failed to parse AI itinerary JSON. Raw response: {}", raw, e);
            throw new BadRequestException(
                    "The AI response could not be understood. Please try generating again.");
        }
    }

    private String extractJson(String raw) {
        String cleaned = raw.trim();
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.replaceAll("(?s)```(json)?", "").trim();
        }
        int start = cleaned.indexOf('{');
        int end = cleaned.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return cleaned.substring(start, end + 1);
        }
        return cleaned;
    }

    private TimeOfDay parseTimeOfDay(String value) {
        try {
            return TimeOfDay.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            return TimeOfDay.MORNING;
        }
    }

    private ActivityCategory parseCategory(String value) {
        try {
            return ActivityCategory.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            return ActivityCategory.OTHER;
        }
    }
}