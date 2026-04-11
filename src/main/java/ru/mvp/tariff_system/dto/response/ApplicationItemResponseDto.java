package ru.mvp.tariff_system.dto.response;

import java.math.BigDecimal;

public record ApplicationItemResponseDto(
        Long serviceParameterId,
        String parameterName,
        String unit,
        Integer volume,
        BigDecimal unitPrice,
        BigDecimal lineTotal
) {
}
