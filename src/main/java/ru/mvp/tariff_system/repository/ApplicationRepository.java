package ru.mvp.tariff_system.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.mvp.tariff_system.entity.Application;
import ru.mvp.tariff_system.entity.ApplicationStatus;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByUserIdAndStatus(Long userId, ApplicationStatus status);

    @EntityGraph(attributePaths = {"tariff"})
    List<Application> findByUserId(Long userId);

    Optional<Application> findByIdAndUserId(Long id, Long userId);
}