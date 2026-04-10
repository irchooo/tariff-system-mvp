package ru.mvp.tariff_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mvp.tariff_system.entity.ServiceParameter;

public interface ServiceParameterRepository extends JpaRepository<ServiceParameter, Long> {
}
