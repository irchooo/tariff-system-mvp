package ru.mvp.tariff_system.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mvp.tariff_system.dto.response.UserResponseDto;
import ru.mvp.tariff_system.entity.User;
import ru.mvp.tariff_system.entity.UserStatus;
import ru.mvp.tariff_system.repository.UserRepository;
import ru.mvp.tariff_system.security.CurrentUserHeaderData;
import ru.mvp.tariff_system.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponseDto getOrCreateCurrentUser(CurrentUserHeaderData currentUser) {
        User user = userRepository.findByKeycloakId(currentUser.keycloakId())
                .orElseGet(() -> createUser(currentUser));

        return toResponseDto(user);
    }

    private User createUser(CurrentUserHeaderData currentUser) {
        validateHeadersForCreate(currentUser);

        User user = new User();
        user.setKeycloakId(currentUser.keycloakId());
        user.setEmail(currentUser.email());
        user.setPhoneNumber(currentUser.phoneNumber());
        user.setFirstName(currentUser.firstName());
        user.setLastName(currentUser.lastName());
        user.setStatus(UserStatus.NEW);

        return userRepository.save(user);
    }

    private void validateHeadersForCreate(CurrentUserHeaderData currentUser) {
        if (currentUser.email() == null || currentUser.email().isBlank()) {
            throw new IllegalArgumentException("Отсутствует email пользователя");
        }
        if (currentUser.phoneNumber() == null || currentUser.phoneNumber().isBlank()) {
            throw new IllegalArgumentException("Отсутствует номер телефона пользователя");
        }
        if (currentUser.firstName() == null || currentUser.firstName().isBlank()) {
            throw new IllegalArgumentException("Отсутствует имя пользователя");
        }
        if (currentUser.lastName() == null || currentUser.lastName().isBlank()) {
            throw new IllegalArgumentException("Отсутствует фамилия пользователя");
        }
    }

    private UserResponseDto toResponseDto(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getKeycloakId(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getStatus()
        );
    }
}
