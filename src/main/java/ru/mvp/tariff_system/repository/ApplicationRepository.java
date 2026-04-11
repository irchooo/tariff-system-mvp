package ru.mvp.tariff_system.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.mvp.tariff_system.entity.Application;
import ru.mvp.tariff_system.entity.ApplicationStatus;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long>, JpaSpecificationExecutor<Application> {

    boolean existsByUserIdAndStatus(Long userId, ApplicationStatus status);

    @EntityGraph(attributePaths = {"tariff"})
    List<Application> findByUserId(Long userId);

    Optional<Application> findByIdAndUserId(Long id, Long userId);

    @Override
    @EntityGraph(attributePaths = {"user", "tariff"})
    List<Application> findAll(org.springframework.data.jpa.domain.Specification<Application> spec,
                              org.springframework.data.domain.Sort sort);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {
            "tariff",
            "items",
            "items.serviceParameter"
    })
    java.util.Optional<Application> findWithItemsByIdAndUserId(Long id, Long userId);

    @org.springframework.data.jpa.repository.EntityGraph(attributePaths = {
            "user",
            "tariff",
            "items",
            "items.serviceParameter"
    })
    java.util.Optional<Application> findWithDetailsById(Long id);
}