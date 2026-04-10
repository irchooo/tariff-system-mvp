package ru.mvp.tariff_system.dto.response;

import ru.mvp.tariff_system.entity.ApplicationStatus;

import java.time.LocalDateTime;

public record ApplicationCancelResponseDto(
        Long id,
        ApplicationStatus status,
        LocalDateTime cancelledAt,
        String message
) {
}