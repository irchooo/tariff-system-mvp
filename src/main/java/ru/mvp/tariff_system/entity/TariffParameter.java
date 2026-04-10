package ru.mvp.tariff_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tariff_parameters")
@Getter
@Setter
public class TariffParameter {

    @EmbeddedId
    private TariffParameterId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tariffId")
    @JoinColumn(name = "tariff_id", nullable = false)
    private Tariff tariff;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("serviceParameterId")
    @JoinColumn(name = "service_parameter_id", nullable = false)
    private ServiceParameter serviceParameter;

    @Column(name = "volume", nullable = false)
    private Integer volume;
}
