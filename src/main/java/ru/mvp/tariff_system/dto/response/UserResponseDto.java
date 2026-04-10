package ru.mvp.tariff_system.dto.response;

import ru.mvp.tariff_system.entity.UserStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDto(
        Long id,
        UUID keycloakId,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        LocalDateTime createdAt,
        UserStatus status
) {
}
