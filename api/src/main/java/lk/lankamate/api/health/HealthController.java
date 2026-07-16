package lk.lankamate.api.health;

import lk.lankamate.api.common.response.ApiResponse;
import lk.lankamate.api.health.dto.HealthResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {

    @GetMapping
    public ApiResponse<HealthResponse> health() {
        HealthResponse payload = new HealthResponse(
                "LankaMate API",
                "UP",
                Instant.now()
        );
        return ApiResponse.ok("Service is healthy", payload);
    }
}