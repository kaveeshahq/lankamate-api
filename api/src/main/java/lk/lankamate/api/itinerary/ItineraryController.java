package lk.lankamate.api.itinerary;

import lk.lankamate.api.auth.CurrentUser;
import lk.lankamate.api.common.response.ApiResponse;
import lk.lankamate.api.itinerary.dto.ItineraryResponseDtos.ItineraryResponse;
import lk.lankamate.api.user.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trips/{tripId}/itinerary")
public class ItineraryController {

    private final ItineraryService itineraryService;

    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
    }

    @PostMapping("/generate")
    public ApiResponse<ItineraryResponse> generate(@PathVariable UUID tripId,
                                                   @CurrentUser User me) {
        return ApiResponse.ok("Itinerary generated",
                itineraryService.generate(tripId, me.getId()));
    }

    @GetMapping
    public ApiResponse<ItineraryResponse> get(@PathVariable UUID tripId,
                                              @CurrentUser User me) {
        return ApiResponse.ok(itineraryService.getForTrip(tripId, me.getId()));
    }
}