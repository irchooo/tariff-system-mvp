package ru.mvp.tariff_system.dto.response;

import java.util.List;

public record ApplicationListResponseDto(
        List<ApplicationListItemResponseDto> applications
) {
}
