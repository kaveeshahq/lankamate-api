package lk.lankamate.api.health.dto;

import java.time.Instant;

public record HealthResponse(
        String service,
        String status,
        Instant serverTime
) {
}