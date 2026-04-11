package ru.mvp.tariff_system.repository;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import ru.mvp.tariff_system.entity.Application;
import ru.mvp.tariff_system.entity.ApplicationStatus;
import ru.mvp.tariff_system.entity.ApplicationType;
import ru.mvp.tariff_system.entity.Tariff;
import ru.mvp.tariff_system.entity.User;

public final class ApplicationSpecifications {

    private ApplicationSpecifications() {
    }

    public static Specification<Application> hasStatus(ApplicationStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Application> clientNameContains(String clientName) {
        return (root, query, cb) -> {
            if (clientName == null || clientName.isBlank()) {
                return null;
            }

            Join<Application, User> userJoin = root.join("user");
            return cb.like(
                    cb.lower(cb.concat(cb.concat(userJoin.get("firstName"), " "), userJoin.get("lastName"))),
                    "%" + clientName.trim().toLowerCase() + "%"
            );
        };
    }

    public static Specification<Application> tariffNameContains(String tariffName) {
        return (root, query, cb) -> {
            if (tariffName == null || tariffName.isBlank()) {
                return null;
            }

            Join<Application, Tariff> tariffJoin = root.join("tariff", jakarta.persistence.criteria.JoinType.LEFT);
            return cb.like(
                    cb.lower(tariffJoin.get("name")),
                    "%" + tariffName.trim().toLowerCase() + "%"
            );
        };
    }

    public static Specification<Application> customOnly(Boolean customOnly) {
        return (root, query, cb) -> {
            if (customOnly == null || !customOnly) {
                return null;
            }

            return cb.equal(root.get("type"), ApplicationType.CUSTOM);
        };
    }
}
