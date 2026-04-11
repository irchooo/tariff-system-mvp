package ru.mvp.tariff_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mvp.tariff_system.dto.request.ApplicationStatusUpdateRequestDto;
import ru.mvp.tariff_system.dto.response.AdminApplicationDetailsResponseDto;
import ru.mvp.tariff_system.dto.response.AdminApplicationListItemResponseDto;
import ru.mvp.tariff_system.dto.response.ApplicationStatusUpdateResponseDto;
import ru.mvp.tariff_system.entity.ApplicationStatus;
import ru.mvp.tariff_system.service.ApplicationService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/applications")
@RequiredArgsConstructor
public class AdminApplicationController {

    private final ApplicationService applicationService;

    @PatchMapping("/{id}/status")
    public ApplicationStatusUpdateResponseDto updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationStatusUpdateRequestDto request
    ) {
        return applicationService.updateApplicationStatus(id, request);
    }

    @GetMapping
    public List<AdminApplicationListItemResponseDto> getAllApplications(
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) String tariffName,
            @RequestParam(required = false) Boolean customOnly,
            @RequestParam(required = false, defaultValue = "desc") String sort
    ) {
        return applicationService.getAllApplicationsForAdmin(
                status,
                clientName,
                tariffName,
                customOnly,
                sort
        );
    }

    @GetMapping("/{id}")
    public AdminApplicationDetailsResponseDto getApplicationDetails(@PathVariable Long id) {
        return applicationService.getApplicationDetailsForAdmin(id);
    }
}
