package ru.mvp.tariff_system.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mvp.tariff_system.dto.request.ApplicationCancelRequestDto;
import ru.mvp.tariff_system.dto.request.ApplicationCreateRequestDto;
import ru.mvp.tariff_system.dto.request.ApplicationParameterRequestDto;
import ru.mvp.tariff_system.dto.request.ApplicationStatusUpdateRequestDto;
import ru.mvp.tariff_system.dto.response.AdminApplicationListItemResponseDto;
import ru.mvp.tariff_system.dto.response.ApplicationCancelResponseDto;
import ru.mvp.tariff_system.dto.response.ApplicationCreateResponseDto;
import ru.mvp.tariff_system.dto.response.ApplicationListItemResponseDto;
import ru.mvp.tariff_system.dto.response.ApplicationListResponseDto;
import ru.mvp.tariff_system.dto.response.ApplicationStatusUpdateResponseDto;
import ru.mvp.tariff_system.dto.response.PaymentResponseDto;
import ru.mvp.tariff_system.entity.Application;
import ru.mvp.tariff_system.entity.ApplicationItem;
import ru.mvp.tariff_system.entity.ApplicationStatus;
import ru.mvp.tariff_system.entity.ApplicationType;
import ru.mvp.tariff_system.entity.ServiceParameter;
import ru.mvp.tariff_system.entity.Tariff;
import ru.mvp.tariff_system.entity.TariffParameter;
import ru.mvp.tariff_system.entity.User;
import ru.mvp.tariff_system.exception.ApplicationCancellationNotAllowedException;
import ru.mvp.tariff_system.exception.ApplicationCreationNotAllowedException;
import ru.mvp.tariff_system.exception.ApplicationNotFoundException;
import ru.mvp.tariff_system.exception.ApplicationPaymentNotAllowedException;
import ru.mvp.tariff_system.exception.ApplicationStatusUpdateNotAllowedException;
import ru.mvp.tariff_system.exception.InvalidRequestException;
import ru.mvp.tariff_system.exception.ServiceParameterNotFoundException;
import ru.mvp.tariff_system.exception.TariffNotFoundException;
import ru.mvp.tariff_system.repository.ApplicationRepository;
import ru.mvp.tariff_system.repository.ServiceParameterRepository;
import ru.mvp.tariff_system.repository.TariffRepository;
import ru.mvp.tariff_system.repository.UserRepository;
import ru.mvp.tariff_system.service.ApplicationService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final TariffRepository tariffRepository;
    private final ServiceParameterRepository serviceParameterRepository;

    @Override
    @Transactional
    public ApplicationCreateResponseDto createApplication(Long userId, ApplicationCreateRequestDto request) {
        validateCreateRequest(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidRequestException("Пользователь не найден"));

        if (applicationRepository.existsByUserIdAndStatus(userId, ApplicationStatus.CONNECTED)) {
            throw new ApplicationCreationNotAllowedException(
                    "Нельзя создать новую заявку, пока у клиента есть подключенный тариф"
            );
        }

        Application application = new Application();
        application.setUser(user);
        application.setStatus(ApplicationStatus.CREATED);

        List<ApplicationItem> items;
        BigDecimal totalCost;

        if (request.tariffId() != null) {
            Tariff tariff = tariffRepository.findByIdAndIsActiveTrue(request.tariffId())
                    .orElseThrow(() -> new TariffNotFoundException("Активный тариф не найден"));

            application.setType(ApplicationType.READY_TARIFF);
            application.setTariff(tariff);

            items = buildItemsFromTariff(application, tariff);
        } else {
            application.setType(ApplicationType.CUSTOM);
            application.setTariff(null);

            items = buildItemsFromCustomParameters(application, request.parameters());
        }

        totalCost = items.stream()
                .map(ApplicationItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        application.setTotalCost(totalCost);
        application.setItems(items);

        Application savedApplication = applicationRepository.save(application);

        return new ApplicationCreateResponseDto(
                savedApplication.getId(),
                savedApplication.getUser().getId(),
                savedApplication.getType(),
                savedApplication.getStatus(),
                savedApplication.getTotalCost(),
                "Заявка успешно создана"
        );
    }

    private void validateCreateRequest(ApplicationCreateRequestDto request) {
        boolean hasTariff = request.tariffId() != null;
        boolean hasParameters = request.parameters() != null && !request.parameters().isEmpty();

        if (hasTariff == hasParameters) {
            throw new InvalidRequestException(
                    "Должен быть указан либо tariffId, либо непустой список parameters"
            );
        }
    }

    private List<ApplicationItem> buildItemsFromTariff(Application application, Tariff tariff) {List<ApplicationItem> items = new ArrayList<>();

        for (TariffParameter tariffParameter : tariff.getTariffParameters()) {
            ServiceParameter serviceParameter = tariffParameter.getServiceParameter();

            BigDecimal unitPrice = serviceParameter.getPricePerUnit();
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(tariffParameter.getVolume()));

            ApplicationItem item = new ApplicationItem();
            item.setApplication(application);
            item.setServiceParameter(serviceParameter);
            item.setParameterName(serviceParameter.getName());
            item.setUnit(serviceParameter.getUnit());
            item.setVolume(tariffParameter.getVolume());
            item.setUnitPrice(unitPrice);
            item.setLineTotal(lineTotal);

            items.add(item);
        }

        return items;
    }

    private List<ApplicationItem> buildItemsFromCustomParameters(
            Application application,
            List<ApplicationParameterRequestDto> parameters
    ) {
        List<ApplicationItem> items = new ArrayList<>();

        for (ApplicationParameterRequestDto parameterRequest : parameters) {
            ServiceParameter serviceParameter = serviceParameterRepository.findById(parameterRequest.serviceParameterId())
                    .orElseThrow(() -> new ServiceParameterNotFoundException(
                            "Параметр услуги не найден: id=" + parameterRequest.serviceParameterId()
                    ));

            BigDecimal unitPrice = serviceParameter.getPricePerUnit();
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(parameterRequest.volume()));

            ApplicationItem item = new ApplicationItem();
            item.setApplication(application);
            item.setServiceParameter(serviceParameter);
            item.setParameterName(serviceParameter.getName());
            item.setUnit(serviceParameter.getUnit());
            item.setVolume(parameterRequest.volume());
            item.setUnitPrice(unitPrice);
            item.setLineTotal(lineTotal);

            items.add(item);
        }

        return items;
    }

    @Override
    @Transactional(readOnly = true)
    public ApplicationListResponseDto getMyApplications(Long userId) {
        List<ApplicationListItemResponseDto> applications = applicationRepository.findByUserId(userId)
                .stream()
                .sorted(this::compareApplicationsForMyList)
                .map(this::toListItemResponseDto)
                .toList();

        return new ApplicationListResponseDto(applications);
    }

    private ApplicationListItemResponseDto toListItemResponseDto(Application application) {
        String tariffName = application.getTariff() != null
                ? application.getTariff().getName()
                : null;

        return new ApplicationListItemResponseDto(
                application.getId(),
                application.getCreatedAt(),
                application.getTotalCost(),
                application.getStatus(),
                application.getType(),
                tariffName,
                application.getContractUrl()
        );
    }

    private int compareApplicationsForMyList(Application first, Application second) {
        boolean firstConnected = first.getStatus() == ApplicationStatus.CONNECTED;
        boolean secondConnected = second.getStatus() == ApplicationStatus.CONNECTED;

        if (firstConnected && !secondConnected) {
            return -1;
        }
        if (!firstConnected && secondConnected) {
            return 1;
        }

        return first.getCreatedAt().compareTo(second.getCreatedAt());
    }

    @Override
    @Transactional
    public ApplicationCancelResponseDto cancelApplication(
            Long userId,
            Long applicationId,
            ApplicationCancelRequestDto request
    ) {
        Application application = applicationRepository.findByIdAndUserId(applicationId, userId)
                .orElseThrow(() -> new ApplicationNotFoundException("Заявка не найдена"));

        if (application.getStatus() != ApplicationStatus.CREATED) {
            throw new ApplicationCancellationNotAllowedException(
                    "Отменить можно только заявку в статусе CREATED"
            );
        }

        application.setStatus(ApplicationStatus.REJECTED);

        Application savedApplication = applicationRepository.save(application);

        return new ApplicationCancelResponseDto(
                savedApplication.getId(),
                savedApplication.getStatus(),
                savedApplication.getUpdatedAt() != null ? savedApplication.getUpdatedAt() : savedApplication.getCreatedAt(),
                "Заявка успешно отменена"
        );
    }

    @Override
    @Transactional
    public PaymentResponseDto payApplication(Long userId, Long applicationId) {
        Application application = applicationRepository.findByIdAndUserId(applicationId, userId)
                .orElseThrow(() -> new ApplicationNotFoundException("Заявка не найдена"));

        if (application.getStatus() != ApplicationStatus.CREATED) {
            throw new ApplicationPaymentNotAllowedException(
                    "Оплатить можно только заявку в статусе CREATED"
            );
        }

        boolean paymentSuccess = emulatePayment();

        if (!paymentSuccess) {
            return new PaymentResponseDto(
                    application.getId(),
                    application.getStatus(),
                    false,
                    "Оплата не прошла, попробуйте позже"
            );
        }

        application.setStatus(ApplicationStatus.PAID);
        Application savedApplication = applicationRepository.save(application);

        return new PaymentResponseDto(
                savedApplication.getId(),
                savedApplication.getStatus(),
                true,
                "Оплата прошла успешно"
        );
    }

    private boolean emulatePayment() {
        return Math.random() < 0.9;
    }

    @Override
    @Transactional
    public ApplicationStatusUpdateResponseDto updateApplicationStatus(
            Long applicationId,
            ApplicationStatusUpdateRequestDto request
    ) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException("Заявка не найдена"));

        if (application.getStatus() != ApplicationStatus.PAID) {
            throw new ApplicationStatusUpdateNotAllowedException(
                    "Изменять статус можно только у заявки в статусе PAID"
            );
        }

        if (request.status() == ApplicationStatus.CONNECTED) {
            application.setStatus(ApplicationStatus.CONNECTED);

            return new ApplicationStatusUpdateResponseDto(
                    application.getId(),
                    application.getStatus(),
                    "Заявка успешно подключена"
            );
        }

        if (request.status() == ApplicationStatus.REJECTED) {

            if (request.reason() == null || request.reason().isBlank()) {
                throw new InvalidRequestException("Для отклонения заявки необходимо указать причину");
            }

            application.setStatus(ApplicationStatus.REJECTED);

            return new ApplicationStatusUpdateResponseDto(
                    application.getId(),
                    application.getStatus(),
                    "Заявка отклонена: " + request.reason()
            );
        }

        throw new ApplicationStatusUpdateNotAllowedException(
                "Допустимые статусы: CONNECTED или REJECTED"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminApplicationListItemResponseDto> getAllApplicationsForAdmin(
            ApplicationStatus status,
            String clientName,
            String tariffName,
            Boolean customOnly,
            String sortDirection
    ) {
        org.springframework.data.domain.Sort sort = buildAdminApplicationsSort(sortDirection);

        org.springframework.data.jpa.domain.Specification<Application> specification =
                org.springframework.data.jpa.domain.Specification.where(
                                ru.mvp.tariff_system.repository.ApplicationSpecifications.hasStatus(status)
                        )
                        .and(ru.mvp.tariff_system.repository.ApplicationSpecifications.clientNameContains(clientName))
                        .and(ru.mvp.tariff_system.repository.ApplicationSpecifications.tariffNameContains(tariffName))
                        .and(ru.mvp.tariff_system.repository.ApplicationSpecifications.customOnly(customOnly));

        return applicationRepository.findAll(specification, sort)
                .stream()
                .map(this::toAdminListItemResponseDto)
                .toList();
    }

    private AdminApplicationListItemResponseDto toAdminListItemResponseDto(Application application) {
        String tariffName = application.getTariff() != null
                ? application.getTariff().getName()
                : null;

        String clientName = application.getUser().getFirstName() + " " + application.getUser().getLastName();

        return new AdminApplicationListItemResponseDto(
                application.getId(),
                application.getUser().getId(),
                clientName,
                application.getCreatedAt(),
                application.getTotalCost(),
                application.getStatus(),
                application.getType(),
                tariffName
        );
    }

    private org.springframework.data.domain.Sort buildAdminApplicationsSort(String sortDirection) {
        if ("asc".equalsIgnoreCase(sortDirection)) {
            return org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.ASC, "createdAt");
        }

        return org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt");
    }

    private String normalizeFilter(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private String normalizeAndLowerFilter(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim().toLowerCase();
    }
}
