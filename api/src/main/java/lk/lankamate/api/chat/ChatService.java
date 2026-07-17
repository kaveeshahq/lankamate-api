package lk.lankamate.api.chat;

import lk.lankamate.api.ai.provider.AiProvider;
import lk.lankamate.api.chat.dto.ChatDtos.ChatMessageResponse;
import lk.lankamate.api.chat.dto.ChatDtos.ConversationResponse;
import lk.lankamate.api.user.User;
import lk.lankamate.api.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private static final String SYSTEM_PERSONA = """
            You are LankaMate, a warm, knowledgeable local Sri Lankan travel guide.
            You help tourists with practical, friendly, accurate advice about
            travelling in Sri Lanka: places, food, transport, culture, safety,
            weather, etiquette, costs, and hidden gems.

            Guidelines:
            - Be concise and conversational (2-5 short sentences unless asked for detail).
            - Give specific, practical, local knowledge — real place names, real tips.
            - If asked about something unrelated to Sri Lanka travel, gently steer back.
            - Never invent unsafe or false information. If unsure, say so.
            """;

    private static final int HISTORY_WINDOW = 10;

    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final AiProvider aiProvider;

    public ChatService(ConversationRepository conversationRepository,
                       UserRepository userRepository,
                       AiProvider aiProvider) {
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.aiProvider = aiProvider;
    }

    @Transactional(readOnly = true)
    public ConversationResponse getConversation(UUID userId) {
        return conversationRepository
                .findFirstByUserIdOrderByCreatedAtDesc(userId)
                .map(this::toResponse)
                .orElseGet(() -> new ConversationResponse(null, List.of()));
    }

    @Transactional
    public ConversationResponse sendMessage(UUID userId, String message) {
        User user = userRepository.getReferenceById(userId);

        Conversation conversation = conversationRepository
                .findFirstByUserIdOrderByCreatedAtDesc(userId)
                .orElseGet(() -> Conversation.builder().user(user).build());

        conversation.addMessage(ChatMessage.builder()
                .role(MessageRole.USER)
                .content(message)
                .build());

        String prompt = buildPrompt(conversation, message);

        String reply;
        try {
            reply = aiProvider.generateText(prompt);
        } catch (Exception e) {
            reply = "Sorry, I couldn't reach my guidebook just now. Please try again.";
        }

        conversation.addMessage(ChatMessage.builder()
                .role(MessageRole.ASSISTANT)
                .content(reply)
                .build());

        Conversation saved = conversationRepository.save(conversation);
        return toResponse(saved);
    }

    private String buildPrompt(Conversation conversation, String newMessage) {
        List<ChatMessage> messages = conversation.getMessages();
        int from = Math.max(0, messages.size() - HISTORY_WINDOW);

        String history = messages.subList(from, messages.size()).stream()
                .map(m -> (m.getRole() == MessageRole.USER ? "Traveller: " : "Guide: ")
                        + m.getContent())
                .collect(Collectors.joining("\n"));

        return SYSTEM_PERSONA
                + "\n\nConversation so far:\n" + history
                + "\n\nTraveller: " + newMessage
                + "\nGuide:";
    }

    private ConversationResponse toResponse(Conversation c) {
        List<ChatMessageResponse> msgs = c.getMessages().stream()
                .map(m -> new ChatMessageResponse(
                        m.getId(),
                        m.getRole().name(),
                        m.getContent(),
                        m.getCreatedAt()))
                .toList();
        return new ConversationResponse(c.getId(), msgs);
    }
}