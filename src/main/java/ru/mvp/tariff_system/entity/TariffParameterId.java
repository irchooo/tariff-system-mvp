package ru.mvp.tariff_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TariffParameterId implements Serializable {

    @Column(name = "tariff_id")
    private Long tariffId;

    @Column(name = "service_parameter_id")
    private Long serviceParameterId;
}
