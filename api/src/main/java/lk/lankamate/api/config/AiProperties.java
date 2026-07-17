package lk.lankamate.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.ai")
public record AiProperties(
        String provider,
        Gemini gemini
) {
    public record Gemini(
            String apiKey,
            String baseUrl,
            String model
    ) {
    }
}