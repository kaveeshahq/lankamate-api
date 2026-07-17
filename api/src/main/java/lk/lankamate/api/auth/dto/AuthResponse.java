package lk.lankamate.api.auth.dto;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        UserSummary user
) {
}