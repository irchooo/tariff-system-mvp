package ru.mvp.tariff_system.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.mvp.tariff_system.entity.Tariff;

import java.util.List;

public interface TariffRepository extends JpaRepository<Tariff, Long> {

    @EntityGraph(attributePaths = {
            "tariffParameters",
            "tariffParameters.serviceParameter"
    })
    List<Tariff> findByIsActiveTrueOrderByIdAsc();
}
