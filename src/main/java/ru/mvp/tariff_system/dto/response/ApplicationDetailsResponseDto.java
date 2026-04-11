package ru.mvp.tariff_system.dto.response;

import ru.mvp.tariff_system.entity.ApplicationStatus;
import ru.mvp.tariff_system.entity.ApplicationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ApplicationDetailsResponseDto(
        Long id,
        LocalDateTime createdAt,
        BigDecimal totalCost,
        ApplicationStatus status,
        ApplicationType type,
        String tariffName,
        List<ApplicationItemResponseDto> items,
        String contractUrl
) {
}
