package ru.mvp.tariff_system.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.mvp.tariff_system.entity.Tariff;

public final class TariffSpecifications {

    private TariffSpecifications() {
    }

    public static Specification<Tariff> nameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) {
                return null;
            }

            return cb.like(
                    cb.lower(root.get("name")),
                    "%" + name.trim().toLowerCase() + "%"
            );
        };
    }

    public static Specification<Tariff> hasIsActive(Boolean isActive) {
        return (root, query, cb) -> {
            if (isActive == null) {
                return null;
            }

            return cb.equal(root.get("isActive"), isActive);
        };
    }
}
