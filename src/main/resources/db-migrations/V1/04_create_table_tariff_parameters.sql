CREATE TABLE tariff_parameters (
                                   tariff_id BIGINT NOT NULL,
                                   service_parameter_id BIGINT NOT NULL,
                                   volume INT NOT NULL,

                                   CONSTRAINT pk_tariff_parameters
                                       PRIMARY KEY (tariff_id, service_parameter_id),

                                   CONSTRAINT fk_tariff_parameters_tariff
                                       FOREIGN KEY (tariff_id)
                                           REFERENCES tariffs(id)
                                           ON DELETE CASCADE,

                                   CONSTRAINT fk_tariff_parameters_service_parameter
                                       FOREIGN KEY (service_parameter_id)
                                           REFERENCES service_parameters(id)
                                           ON DELETE RESTRICT,

                                   CONSTRAINT chk_tariff_parameters_volume
                                       CHECK (volume > 0)
);