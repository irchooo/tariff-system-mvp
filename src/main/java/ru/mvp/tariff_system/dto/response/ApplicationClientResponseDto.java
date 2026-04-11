package ru.mvp.tariff_system.dto.response;

public record ApplicationClientResponseDto(
        Long id,
        String firstName,
        String lastName,
        String phoneNumber,
        String email
) {
}
