package ru.mvp.tariff_system.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mvp.tariff_system.dto.request.AdminTariffCreateRequestDto;
import ru.mvp.tariff_system.dto.request.AdminTariffParameterRequestDto;
import ru.mvp.tariff_system.dto.request.AdminTariffStatusUpdateRequestDto;
import ru.mvp.tariff_system.dto.response.AdminTariffCreateResponseDto;
import ru.mvp.tariff_system.dto.response.AdminTariffListItemResponseDto;
import ru.mvp.tariff_system.dto.response.AdminTariffStatusUpdateResponseDto;
import ru.mvp.tariff_system.dto.response.TariffResponseDto;
import ru.mvp.tariff_system.dto.response.TariffServiceParameterResponseDto;
import ru.mvp.tariff_system.entity.ServiceParameter;
import ru.mvp.tariff_system.entity.Tariff;
import ru.mvp.tariff_system.entity.TariffParameter;
import ru.mvp.tariff_system.entity.TariffParameterId;
import ru.mvp.tariff_system.exception.InvalidRequestException;
import ru.mvp.tariff_system.exception.ServiceParameterNotFoundException;
import ru.mvp.tariff_system.exception.TariffAlreadyExistsException;
import ru.mvp.tariff_system.exception.TariffNotFoundException;
import ru.mvp.tariff_system.repository.ServiceParameterRepository;
import ru.mvp.tariff_system.repository.TariffRepository;
import ru.mvp.tariff_system.service.TariffService;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;
    private final ServiceParameterRepository serviceParameterRepository;

    @Override
    public List<TariffResponseDto> getActiveTariffs() {
        return tariffRepository.findByIsActiveTrueOrderByIdAsc()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    private TariffResponseDto toResponseDto(Tariff tariff) {
        List<TariffServiceParameterResponseDto> serviceParameters = tariff.getTariffParameters()
                .stream()
                .sorted(Comparator.comparing(tp -> tp.getServiceParameter().getId()))
                .map(this::toServiceParameterDto)
                .toList();

        BigDecimal totalPrice = tariff.getTariffParameters()
                .stream()
                .map(this::calculateLinePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new TariffResponseDto(
                tariff.getId(),
                tariff.getName(),
                tariff.getDescription(),
                totalPrice,
                serviceParameters
        );
    }

    private TariffServiceParameterResponseDto toServiceParameterDto(TariffParameter tariffParameter) {
        return new TariffServiceParameterResponseDto(
                tariffParameter.getServiceParameter().getId(),
                tariffParameter.getServiceParameter().getName(),
                tariffParameter.getServiceParameter().getUnit(),
                tariffParameter.getVolume()
        );
    }

    private BigDecimal calculateLinePrice(TariffParameter tariffParameter) {
        BigDecimal volume = BigDecimal.valueOf(tariffParameter.getVolume());
        BigDecimal pricePerUnit = tariffParameter.getServiceParameter().getPricePerUnit();
        return pricePerUnit.multiply(volume);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminTariffListItemResponseDto> getAllTariffsForAdmin(
            String name,
            Boolean isActive,
            String sortDirection
    ) {
        org.springframework.data.domain.Sort sort = buildAdminTariffsSort(sortDirection);

        org.springframework.data.jpa.domain.Specification<Tariff> specification =
                org.springframework.data.jpa.domain.Specification.where(
                                ru.mvp.tariff_system.repository.TariffSpecifications.nameContains(name)
                        )
                        .and(ru.mvp.tariff_system.repository.TariffSpecifications.hasIsActive(isActive));

        return tariffRepository.findAll(specification, sort)
                .stream()
                .map(this::toAdminTariffListItemResponseDto)
                .toList();
    }

    private AdminTariffListItemResponseDto toAdminTariffListItemResponseDto(Tariff tariff) {
        BigDecimal totalPrice = tariff.getTariffParameters()
                .stream()
                .map(this::calculateLinePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new AdminTariffListItemResponseDto(
                tariff.getId(),
                tariff.getName(),
                tariff.getDescription(),
                tariff.getIsActive(),
                totalPrice
        );
    }

    private org.springframework.data.domain.Sort buildAdminTariffsSort(String sortDirection) {
        if ("asc".equalsIgnoreCase(sortDirection)) {
            return org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.ASC, "name");
        }

        return org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "name");
    }

    private String normalizeTariffFilter(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    @Override
    @Transactional
    public AdminTariffCreateResponseDto createTariff(AdminTariffCreateRequestDto request) {
        validateTariffCreateRequest(request);

        if (tariffRepository.existsByNameIgnoreCase(request.name().trim())) {
            throw new TariffAlreadyExistsException("Тариф с таким названием уже существует");
        }

        Tariff tariff = new Tariff();
        tariff.setName(request.name().trim());
        tariff.setDescription(request.description());
        tariff.setIsActive(true);

        List<TariffParameter> tariffParameters = new java.util.ArrayList<>();

        for (AdminTariffParameterRequestDto parameterRequest : request.serviceParameters()) {
            ServiceParameter serviceParameter = serviceParameterRepository.findById(parameterRequest.serviceParameterId())
                    .orElseThrow(() -> new ServiceParameterNotFoundException(
                            "Параметр услуги не найден: id=" + parameterRequest.serviceParameterId()
                    ));

            TariffParameter tariffParameter = new TariffParameter();
            tariffParameter.setId(new TariffParameterId(null, serviceParameter.getId()));
            tariffParameter.setTariff(tariff);
            tariffParameter.setServiceParameter(serviceParameter);
            tariffParameter.setVolume(parameterRequest.volume());

            tariffParameters.add(tariffParameter);
        }

        tariff.setTariffParameters(tariffParameters);

        Tariff savedTariff = tariffRepository.save(tariff);

        for (TariffParameter tariffParameter : savedTariff.getTariffParameters()) {
            tariffParameter.setId(new TariffParameterId(
                    savedTariff.getId(),
                    tariffParameter.getServiceParameter().getId()
            ));
        }

        BigDecimal totalPrice = savedTariff.getTariffParameters()
                .stream()
                .map(this::calculateLinePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new AdminTariffCreateResponseDto(
                savedTariff.getId(),
                savedTariff.getName(),
                savedTariff.getDescription(),
                savedTariff.getIsActive(),
                totalPrice,
                "Тариф успешно создан"
        );
    }

    private void validateTariffCreateRequest(AdminTariffCreateRequestDto request) {
        java.util.Set<Long> uniqueIds = new java.util.HashSet<>();

        for (AdminTariffParameterRequestDto parameter : request.serviceParameters()) {
            if (!uniqueIds.add(parameter.serviceParameterId())) {
                throw new InvalidRequestException(
                        "Параметры тарифа не должны дублироваться. Повторяется serviceParameterId="
                                + parameter.serviceParameterId()
                );
            }
        }
    }

    @Override
    @Transactional
    public AdminTariffStatusUpdateResponseDto updateTariffStatus(
            Long tariffId,
            AdminTariffStatusUpdateRequestDto request
    ) {
        Tariff tariff = tariffRepository.findById(tariffId)
                .orElseThrow(() -> new TariffNotFoundException("Тариф не найден"));

        tariff.setIsActive(request.isActive());

        Tariff savedTariff = tariffRepository.save(tariff);

        String message = Boolean.TRUE.equals(savedTariff.getIsActive())
                ? "Тариф успешно активирован"
                : "Тариф успешно архивирован";

        return new AdminTariffStatusUpdateResponseDto(
                savedTariff.getId(),
                savedTariff.getName(),
                savedTariff.getIsActive(),
                message
        );
    }
}
