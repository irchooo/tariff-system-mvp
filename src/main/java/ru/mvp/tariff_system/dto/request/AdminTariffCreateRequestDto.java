package ru.mvp.tariff_system.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record AdminTariffCreateRequestDto(
        @NotBlank
        String name,

        String description,

        @Valid
        @NotEmpty
        List<AdminTariffParameterRequestDto> serviceParameters
) {
}
