package ru.mvp.tariff_system.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import ru.mvp.tariff_system.util.HeaderEncodingUtils;

import java.util.UUID;

@Component
public class CurrentUserHeaderExtractor {

    public CurrentUserHeaderData extract(HttpServletRequest request) {
        String keycloakIdHeader = request.getHeader("X-User-Id");
        String email = request.getHeader("X-User-Email");
        String phoneNumber = request.getHeader("X-User-Phone");
        String encodedFirstName = request.getHeader("X-User-FirstName");
        String encodedLastName = request.getHeader("X-User-LastName");

        if (keycloakIdHeader == null || keycloakIdHeader.isBlank()) {
            throw new IllegalArgumentException("Отсутствует заголовок X-User-Id");
        }

        UUID keycloakId = UUID.fromString(keycloakIdHeader);

        return new CurrentUserHeaderData(
                keycloakId,
                email,
                phoneNumber,
                HeaderEncodingUtils.decodeBase64Header(encodedFirstName),
                HeaderEncodingUtils.decodeBase64Header(encodedLastName)
        );
    }
}
