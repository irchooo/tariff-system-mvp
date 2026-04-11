package ru.mvp.tariff_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mvp.tariff_system.dto.response.AdminTariffListItemResponseDto;
import ru.mvp.tariff_system.service.TariffService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/tariffs")
@RequiredArgsConstructor
public class AdminTariffController {

    private final TariffService tariffService;

    @GetMapping
    public List<AdminTariffListItemResponseDto> getAllTariffs(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false, defaultValue = "asc") String sort
    ) {
        return tariffService.getAllTariffsForAdmin(name, isActive, sort);
    }
}
