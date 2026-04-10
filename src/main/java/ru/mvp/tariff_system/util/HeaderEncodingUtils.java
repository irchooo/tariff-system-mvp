package ru.mvp.tariff_system.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class HeaderEncodingUtils {

    private HeaderEncodingUtils() {
    }

    public static String decodeBase64Header(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return new String(
                Base64.getUrlDecoder().decode(value),
                StandardCharsets.UTF_8
        );
    }
}
