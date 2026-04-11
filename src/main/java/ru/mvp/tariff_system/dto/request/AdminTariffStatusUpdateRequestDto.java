package ru.mvp.tariff_system.dto.request;

import jakarta.validation.constraints.NotNull;

public record AdminTariffStatusUpdateRequestDto(
        @NotNull
        Boolean isActive
) {
}
