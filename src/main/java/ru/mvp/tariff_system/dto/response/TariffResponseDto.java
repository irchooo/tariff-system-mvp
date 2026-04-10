package ru.mvp.tariff_system.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record TariffResponseDto(
        Long id,
        String name,
        String description,
        BigDecimal totalPrice,
        List<TariffServiceParameterResponseDto> serviceParameters
) {
}
