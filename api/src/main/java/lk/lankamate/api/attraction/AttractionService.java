package lk.lankamate.api.attraction;

import lk.lankamate.api.attraction.dto.AttractionResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AttractionService {

    private final AttractionRepository repository;

    public AttractionService(AttractionRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<AttractionResponse> findAll(String type) {
        List<Attraction> results;
        if (type != null && !type.isBlank()) {
            try {
                results = repository.findByType(
                        AttractionType.valueOf(type.trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                results = List.of();
            }
        } else {
            results = repository.findAll();
        }
        return results.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<AttractionResponse> findNearby(double lat, double lng, double radiusKm) {
        return repository.findNearby(lat, lng, radiusKm * 1000).stream()
                .map(this::toResponse)
                .toList();
    }

    private AttractionResponse toResponse(Attraction a) {
        return new AttractionResponse(
                a.getId(),
                a.getName(),
                a.getDescription(),
                a.getType().name(),
                a.getRegion(),
                a.getLatitude(),
                a.getLongitude(),
                a.getRating(),
                a.getEntryFee(),
                a.getBestTime(),
                a.getTip(),
                a.getImageUrl()
        );
    }
}