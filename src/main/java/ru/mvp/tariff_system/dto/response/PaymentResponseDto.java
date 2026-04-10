package ru.mvp.tariff_system.dto.response;

import ru.mvp.tariff_system.entity.ApplicationStatus;

public record PaymentResponseDto(
        Long applicationId,
        ApplicationStatus newStatus,
        boolean success,
        String message
) {
}
