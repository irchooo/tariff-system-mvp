package ru.mvp.tariff_system.dto.request;

import java.util.List;

public record ApplicationCreateRequestDto(
        Long tariffId,
        List<ApplicationParameterRequestDto> parameters
) {
}
