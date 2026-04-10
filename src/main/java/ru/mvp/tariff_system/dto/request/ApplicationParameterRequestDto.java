package ru.mvp.tariff_system.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ApplicationParameterRequestDto(
        @NotNull
        Long serviceParameterId,

        @NotNull
        @Min(1)
        Integer volume
) {
}
