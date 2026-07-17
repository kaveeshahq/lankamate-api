package lk.lankamate.api.chat;

import jakarta.validation.Valid;
import lk.lankamate.api.auth.CurrentUser;
import lk.lankamate.api.chat.dto.ChatDtos.ConversationResponse;
import lk.lankamate.api.chat.dto.ChatDtos.SendMessageRequest;
import lk.lankamate.api.common.response.ApiResponse;
import lk.lankamate.api.user.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public ApiResponse<ConversationResponse> history(@CurrentUser User me) {
        return ApiResponse.ok(chatService.getConversation(me.getId()));
    }

    @PostMapping
    public ApiResponse<ConversationResponse> send(
            @Valid @RequestBody SendMessageRequest request,
            @CurrentUser User me) {
        return ApiResponse.ok(chatService.sendMessage(me.getId(), request.message()));
    }
}

