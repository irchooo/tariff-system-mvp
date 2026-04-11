package ru.mvp.tariff_system.dto.response;

import ru.mvp.tariff_system.entity.ApplicationStatus;

public record ApplicationStatusUpdateResponseDto(
        Long applicationId,
        ApplicationStatus status,
        String message
) {
}
