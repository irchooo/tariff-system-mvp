package ru.mvp.tariff_system.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.mvp.tariff_system.entity.Tariff;

import java.util.List;
import java.util.Optional;

public interface TariffRepository extends JpaRepository<Tariff, Long>, JpaSpecificationExecutor<Tariff> {

    @EntityGraph(attributePaths = {
            "tariffParameters",
            "tariffParameters.serviceParameter"
    })
    List<Tariff> findByIsActiveTrueOrderByIdAsc();

    @EntityGraph(attributePaths = {
            "tariffParameters",
            "tariffParameters.serviceParameter"
    })
    Optional<Tariff> findByIdAndIsActiveTrue(Long id);

    @Override
    @EntityGraph(attributePaths = {
            "tariffParameters",
            "tariffParameters.serviceParameter"
    })
    List<Tariff> findAll(org.springframework.data.jpa.domain.Specification<Tariff> spec,
                         org.springframework.data.domain.Sort sort);
}
