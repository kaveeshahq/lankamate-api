package lk.lankamate.api.trip;

import jakarta.validation.Valid;
import lk.lankamate.api.auth.CurrentUser;
import lk.lankamate.api.common.response.ApiResponse;
import lk.lankamate.api.trip.dto.CreateTripRequest;
import lk.lankamate.api.trip.dto.TripResponse;
import lk.lankamate.api.trip.dto.UpdateTripRequest;
import lk.lankamate.api.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TripResponse>> create(
            @Valid @RequestBody CreateTripRequest request,
            @CurrentUser User me) {

        TripResponse created = tripService.create(request, me);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Trip created", created));
    }

    @GetMapping
    public ApiResponse<List<TripResponse>> listMine(@CurrentUser User me) {
        return ApiResponse.ok(tripService.listMyTrips(me.getId()));
    }

    @GetMapping("/{id}")
    public ApiResponse<TripResponse> getOne(@PathVariable UUID id,
                                            @CurrentUser User me) {
        return ApiResponse.ok(tripService.getById(id, me.getId()));
    }

    @PutMapping("/{id}")
    public ApiResponse<TripResponse> update(@PathVariable UUID id,
                                            @Valid @RequestBody UpdateTripRequest request,
                                            @CurrentUser User me) {
        return ApiResponse.ok("Trip updated", tripService.update(id, request, me.getId()));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable UUID id, @CurrentUser User me) {
        tripService.delete(id, me.getId());
        return ApiResponse.ok("Trip deleted", null);
    }
}