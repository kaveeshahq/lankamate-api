package lk.lankamate.api.chat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class ChatDtos {

    private ChatDtos() {
    }

    public record SendMessageRequest(
            @NotBlank(message = "Message cannot be empty")
            @Size(max = 2000, message = "Message is too long")
            String message
    ) {
    }

    public record ChatMessageResponse(
            UUID id,
            String role,
            String content,
            Instant createdAt
    ) {
    }

    public record ConversationResponse(
            UUID id,
            List<ChatMessageResponse> messages
    ) {
    }
}