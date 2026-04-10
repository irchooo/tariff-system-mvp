package ru.mvp.tariff_system.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mvp.tariff_system.dto.response.UserResponseDto;
import ru.mvp.tariff_system.security.CurrentUserHeaderData;
import ru.mvp.tariff_system.security.CurrentUserHeaderExtractor;
import ru.mvp.tariff_system.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final CurrentUserHeaderExtractor currentUserHeaderExtractor;

    @GetMapping("/me")
    public UserResponseDto getCurrentUser(HttpServletRequest request) {
        CurrentUserHeaderData currentUser = currentUserHeaderExtractor.extract(request);
        return userService.getOrCreateCurrentUser(currentUser);
    }
}
