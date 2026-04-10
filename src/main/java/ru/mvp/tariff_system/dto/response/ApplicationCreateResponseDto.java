package ru.mvp.tariff_system.dto.response;

import ru.mvp.tariff_system.entity.ApplicationStatus;
import ru.mvp.tariff_system.entity.ApplicationType;

import java.math.BigDecimal;

public record ApplicationCreateResponseDto(
        Long id,
        Long userId,
        ApplicationType type,
        ApplicationStatus status,
        BigDecimal totalCost,
        String message
) {
}
