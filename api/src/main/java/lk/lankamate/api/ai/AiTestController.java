package lk.lankamate.api.ai;

import lk.lankamate.api.ai.provider.AiProvider;
import lk.lankamate.api.common.response.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ai")
public class AiTestController {

    private final AiProvider aiProvider;

    public AiTestController(AiProvider aiProvider) {
        this.aiProvider = aiProvider;
    }

    @PostMapping("/ping")
    public ApiResponse<Map<String, String>> ping(@RequestBody Map<String, String> body) {
        String prompt = body.getOrDefault("prompt", "Say hello from Sri Lanka in one sentence.");
        String reply = aiProvider.generateText(prompt);
        return ApiResponse.ok(Map.of(
                "provider", aiProvider.name(),
                "reply", reply
        ));
    }
}