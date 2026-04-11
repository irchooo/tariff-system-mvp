package ru.mvp.tariff_system.service;

import ru.mvp.tariff_system.dto.response.AdminTariffListItemResponseDto;
import ru.mvp.tariff_system.dto.response.TariffResponseDto;

import java.util.List;

public interface TariffService {

    List<TariffResponseDto> getActiveTariffs();
    List<AdminTariffListItemResponseDto> getAllTariffsForAdmin(
            String name,
            Boolean isActive,
            String sortDirection
    );
}
