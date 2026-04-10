package ru.mvp.tariff_system.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mvp.tariff_system.dto.response.TariffResponseDto;
import ru.mvp.tariff_system.dto.response.TariffServiceParameterResponseDto;
import ru.mvp.tariff_system.entity.Tariff;
import ru.mvp.tariff_system.entity.TariffParameter;
import ru.mvp.tariff_system.repository.TariffRepository;
import ru.mvp.tariff_system.service.TariffService;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;

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
}
