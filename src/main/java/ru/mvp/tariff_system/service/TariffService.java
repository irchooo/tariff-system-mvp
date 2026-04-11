package ru.mvp.tariff_system.service;

import ru.mvp.tariff_system.dto.request.AdminTariffCreateRequestDto;
import ru.mvp.tariff_system.dto.request.AdminTariffStatusUpdateRequestDto;
import ru.mvp.tariff_system.dto.response.AdminTariffCreateResponseDto;
import ru.mvp.tariff_system.dto.response.AdminTariffListItemResponseDto;
import ru.mvp.tariff_system.dto.response.AdminTariffStatusUpdateResponseDto;
import ru.mvp.tariff_system.dto.response.TariffResponseDto;

import java.util.List;

public interface TariffService {

    List<TariffResponseDto> getActiveTariffs();
    List<AdminTariffListItemResponseDto> getAllTariffsForAdmin(
            String name,
            Boolean isActive,
            String sortDirection
    );

    AdminTariffCreateResponseDto createTariff(AdminTariffCreateRequestDto request);

    AdminTariffStatusUpdateResponseDto updateTariffStatus(
            Long tariffId,
            AdminTariffStatusUpdateRequestDto request
    );
}
