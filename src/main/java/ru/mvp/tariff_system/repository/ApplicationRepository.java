package ru.mvp.tariff_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mvp.tariff_system.entity.Application;
import ru.mvp.tariff_system.entity.ApplicationStatus;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByUserIdAndStatus(Long userId, ApplicationStatus status);
}