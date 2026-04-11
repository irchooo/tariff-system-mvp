package ru.mvp.tariff_system.service;

import ru.mvp.tariff_system.dto.request.ApplicationCancelRequestDto;
import ru.mvp.tariff_system.dto.request.ApplicationCreateRequestDto;
import ru.mvp.tariff_system.dto.request.ApplicationStatusUpdateRequestDto;
import ru.mvp.tariff_system.dto.response.AdminApplicationListItemResponseDto;
import ru.mvp.tariff_system.dto.response.ApplicationCancelResponseDto;
import ru.mvp.tariff_system.dto.response.ApplicationCreateResponseDto;
import ru.mvp.tariff_system.dto.response.ApplicationListResponseDto;
import ru.mvp.tariff_system.dto.response.ApplicationStatusUpdateResponseDto;
import ru.mvp.tariff_system.dto.response.PaymentResponseDto;
import ru.mvp.tariff_system.entity.ApplicationStatus;

import java.util.List;

public interface ApplicationService {

    ApplicationCreateResponseDto createApplication(Long userId, ApplicationCreateRequestDto request);
    ApplicationListResponseDto getMyApplications(Long userId);
    ApplicationCancelResponseDto cancelApplication(Long userId, Long applicationId, ApplicationCancelRequestDto request);
    PaymentResponseDto payApplication(Long userId, Long applicationId);
    ApplicationStatusUpdateResponseDto updateApplicationStatus(
            Long applicationId,
            ApplicationStatusUpdateRequestDto request
    );

    List<AdminApplicationListItemResponseDto> getAllApplicationsForAdmin(
            ApplicationStatus status,
            String clientName,
            String tariffName,
            Boolean customOnly,
            String sortDirection
    );
}
