package ru.mvp.tariff_system.dto.response;

public record TariffServiceParameterResponseDto(
        Long serviceParameterId,
        String serviceParameterName,
        String unit,
        Integer volume
) {
}
