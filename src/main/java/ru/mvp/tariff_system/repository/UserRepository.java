package ru.mvp.tariff_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mvp.tariff_system.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByKeycloakId(UUID keycloakId);
}
