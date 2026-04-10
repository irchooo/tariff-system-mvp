package ru.mvp.tariff_system.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mvp.tariff_system.dto.request.ApplicationCreateRequestDto;
import ru.mvp.tariff_system.dto.response.ApplicationCreateResponseDto;
import ru.mvp.tariff_system.security.CurrentUserHeaderData;
import ru.mvp.tariff_system.security.CurrentUserHeaderExtractor;
import ru.mvp.tariff_system.service.ApplicationService;
import ru.mvp.tariff_system.service.UserService;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final CurrentUserHeaderExtractor currentUserHeaderExtractor;
    private final UserService userService;

    @PostMapping
    public ApplicationCreateResponseDto createApplication(
            @Valid @RequestBody ApplicationCreateRequestDto request,
            HttpServletRequest httpRequest
    ) {
        CurrentUserHeaderData currentUser = currentUserHeaderExtractor.extract(httpRequest);

        Long userId = userService.getOrCreateCurrentUser(currentUser).id();

        return applicationService.createApplication(userId, request);
    }
}
