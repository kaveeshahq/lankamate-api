package lk.lankamate.api.auth.dto;

import lk.lankamate.api.user.User;

import java.util.UUID;

public record UserSummary(
        UUID id,
        String name,
        String email,
        String role,
        String avatarUrl
) {
    public static UserSummary from(User user) {
        return new UserSummary(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().name(),
                user.getAvatarUrl()
        );
    }
}