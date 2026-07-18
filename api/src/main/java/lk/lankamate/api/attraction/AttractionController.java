package lk.lankamate.api.attraction;

import lk.lankamate.api.attraction.dto.AttractionResponse;
import lk.lankamate.api.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/attractions")
public class AttractionController {

    private final AttractionService attractionService;

    public AttractionController(AttractionService attractionService) {
        this.attractionService = attractionService;
    }

    /** All attractions, optionally filtered by type. */
    @GetMapping
    public ApiResponse<List<AttractionResponse>> list(
            @RequestParam(required = false) String type) {
        return ApiResponse.ok(attractionService.findAll(type));
    }

    /** Attractions near a point, nearest first (PostGIS distance). */
    @GetMapping("/nearby")
    public ApiResponse<List<AttractionResponse>> nearby(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "50") double radiusKm) {
        return ApiResponse.ok(attractionService.findNearby(lat, lng, radiusKm));
    }
}