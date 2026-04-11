package ru.mvp.tariff_system.dto.response;

import ru.mvp.tariff_system.entity.ApplicationStatus;
import ru.mvp.tariff_system.entity.ApplicationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AdminApplicationListItemResponseDto(
        Long id,
        Long userId,
        String clientName,
        LocalDateTime createdAt,
        BigDecimal totalCost,
        ApplicationStatus status,
        ApplicationType type,
        String tariffName
) {
}
