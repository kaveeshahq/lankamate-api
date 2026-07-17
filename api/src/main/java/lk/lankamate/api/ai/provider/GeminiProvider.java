package lk.lankamate.api.ai.provider;

import lk.lankamate.api.ai.dto.GeminiDtos;
import lk.lankamate.api.common.exception.BadRequestException;
import lk.lankamate.api.config.AiProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
public class GeminiProvider implements AiProvider {

    private final AiProperties properties;
    private final RestClient restClient;

    public GeminiProvider(AiProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.create();
    }

    @Override
    public String generateText(String prompt) {
        AiProperties.Gemini config = properties.gemini();

        if (config == null || config.apiKey() == null || config.apiKey().isBlank()) {
            throw new BadRequestException("AI is not configured: missing Gemini API key");
        }

        GeminiDtos.Request body = new GeminiDtos.Request(
                List.of(new GeminiDtos.Content(
                        List.of(new GeminiDtos.Part(prompt)))));

        String url = UriComponentsBuilder
                .fromUriString(config.baseUrl())
                .path("/models/" + config.model() + ":generateContent")
                .queryParam("key", config.apiKey())
                .build()
                .toUriString();

        GeminiDtos.Response response = restClient.post()
                .uri(url)
                .body(body)
                .retrieve()
                .body(GeminiDtos.Response.class);

        return extractText(response);
    }

    @Override
    public String name() {
        return "gemini";
    }

    private String extractText(GeminiDtos.Response response) {
        if (response == null
                || response.candidates() == null
                || response.candidates().isEmpty()) {
            throw new BadRequestException("AI returned no candidates");
        }

        GeminiDtos.Candidate first = response.candidates().get(0);
        if (first.content() == null
                || first.content().parts() == null
                || first.content().parts().isEmpty()) {
            throw new BadRequestException("AI returned an empty response");
        }

        return first.content().parts().get(0).text();
    }
}