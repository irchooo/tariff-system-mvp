package ru.mvp.tariff_system.security;

import java.util.UUID;

public record CurrentUserHeaderData(
        UUID keycloakId,
        String email,
        String phoneNumber,
        String firstName,
        String lastName
) {
}
