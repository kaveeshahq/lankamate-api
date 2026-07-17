package lk.lankamate.api.ai.provider;

public interface AiProvider {

    String generateText(String prompt);

    String name();
}