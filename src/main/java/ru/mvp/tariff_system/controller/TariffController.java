package ru.mvp.tariff_system.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mvp.tariff_system.dto.response.TariffResponseDto;
import ru.mvp.tariff_system.service.TariffService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tariffs")
@RequiredArgsConstructor
public class TariffController {

    private final TariffService tariffService;

    @GetMapping
    public List<TariffResponseDto> getActiveTariffs() {
        return tariffService.getActiveTariffs();
    }
}
