package lk.lankamate.api.trip;

import lk.lankamate.api.common.exception.BadRequestException;
import lk.lankamate.api.common.exception.ResourceNotFoundException;
import lk.lankamate.api.trip.dto.CreateTripRequest;
import lk.lankamate.api.trip.dto.TripResponse;
import lk.lankamate.api.trip.dto.UpdateTripRequest;
import lk.lankamate.api.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class TripService {

    private final TripRepository tripRepository;
    private final TripMapper tripMapper;

    public TripService(TripRepository tripRepository, TripMapper tripMapper) {
        this.tripRepository = tripRepository;
        this.tripMapper = tripMapper;
    }

    @Transactional
    public TripResponse create(CreateTripRequest request, User owner) {
        validateDates(request.startDate(), request.endDate());

        Trip trip = Trip.builder()
                .user(owner)
                .title(request.title())
                .description(request.description())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .baseLocationName(request.baseLocationName())
                .baseLatitude(request.baseLatitude())
                .baseLongitude(request.baseLongitude())
                .totalBudget(request.totalBudget())
                .currency(request.currency() != null ? request.currency() : "USD")
                .travelStyle(request.travelStyle() != null
                        ? request.travelStyle() : TravelStyle.BALANCED)
                .status(TripStatus.PLANNING)
                .interests(request.interests() != null
                        ? new HashSet<>(request.interests()) : new HashSet<>())
                .build();

        return tripMapper.toResponse(tripRepository.save(trip));
    }

    @Transactional(readOnly = true)
    public List<TripResponse> listMyTrips(UUID ownerId) {
        return tripRepository.findByUserIdOrderByCreatedAtDesc(ownerId)
                .stream()
                .map(tripMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TripResponse getById(UUID tripId, UUID ownerId) {
        Trip trip = requireOwnedTrip(tripId, ownerId);
        return tripMapper.toResponse(trip);
    }

    @Transactional
    public TripResponse update(UUID tripId, UpdateTripRequest request, UUID ownerId) {
        validateDates(request.startDate(), request.endDate());

        Trip trip = requireOwnedTrip(tripId, ownerId);

        trip.setTitle(request.title());
        trip.setDescription(request.description());
        trip.setStartDate(request.startDate());
        trip.setEndDate(request.endDate());
        trip.setBaseLocationName(request.baseLocationName());
        trip.setBaseLatitude(request.baseLatitude());
        trip.setBaseLongitude(request.baseLongitude());
        trip.setTotalBudget(request.totalBudget());
        if (request.currency() != null) {
            trip.setCurrency(request.currency());
        }
        if (request.travelStyle() != null) {
            trip.setTravelStyle(request.travelStyle());
        }
        if (request.status() != null) {
            trip.setStatus(request.status());
        }
        trip.setInterests(request.interests() != null
                ? new HashSet<>(request.interests()) : new HashSet<>());

        return tripMapper.toResponse(tripRepository.save(trip));
    }

    @Transactional
    public void delete(UUID tripId, UUID ownerId) {
        Trip trip = requireOwnedTrip(tripId, ownerId);
        tripRepository.delete(trip);
    }

    private Trip requireOwnedTrip(UUID tripId, UUID ownerId) {
        return tripRepository.findByIdAndUserId(tripId, ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip", tripId));
    }

    private void validateDates(java.time.LocalDate start, java.time.LocalDate end) {
        if (end.isBefore(start)) {
            throw new BadRequestException("End date must be on or after the start date");
        }
    }
}