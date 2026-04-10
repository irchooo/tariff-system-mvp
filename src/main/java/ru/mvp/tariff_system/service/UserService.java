package ru.mvp.tariff_system.service;

import ru.mvp.tariff_system.dto.response.UserResponseDto;
import ru.mvp.tariff_system.security.CurrentUserHeaderData;

public interface UserService {

    UserResponseDto getOrCreateCurrentUser(CurrentUserHeaderData currentUser);
}
