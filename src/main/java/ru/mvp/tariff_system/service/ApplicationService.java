package ru.mvp.tariff_system.service;

import ru.mvp.tariff_system.dto.request.ApplicationCreateRequestDto;
import ru.mvp.tariff_system.dto.response.ApplicationCreateResponseDto;

public interface ApplicationService {

    ApplicationCreateResponseDto createApplication(Long userId, ApplicationCreateRequestDto request);
}
