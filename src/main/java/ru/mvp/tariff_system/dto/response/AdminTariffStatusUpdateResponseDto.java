package ru.mvp.tariff_system.dto.response;

public record AdminTariffStatusUpdateResponseDto(
        Long id,
        String name,
        Boolean isActive,
        String message
) {
}
