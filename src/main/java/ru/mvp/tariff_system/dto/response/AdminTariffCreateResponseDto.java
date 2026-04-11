package ru.mvp.tariff_system.dto.response;

import java.math.BigDecimal;

public record AdminTariffCreateResponseDto(
        Long id,
        String name,
        String description,
        Boolean isActive,
        BigDecimal totalPrice,
        String message
) {
}
