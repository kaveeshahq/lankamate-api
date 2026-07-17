package lk.lankamate.api.ai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public final class GeminiDtos {

    private GeminiDtos() {
    }

    public record Request(List<Content> contents) {
    }

    public record Content(List<Part> parts) {
    }

    public record Part(String text) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(List<Candidate> candidates) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Candidate(Content content) {
    }
}