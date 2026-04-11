package ru.mvp.tariff_system.dto.request;

import jakarta.validation.constraints.NotNull;
import ru.mvp.tariff_system.entity.ApplicationStatus;

public record ApplicationStatusUpdateRequestDto(

        @NotNull
        ApplicationStatus status,

        String reason
) {
}