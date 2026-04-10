CREATE TABLE application_items (
                                   id BIGSERIAL PRIMARY KEY,
                                   application_id BIGINT NOT NULL,
                                   service_parameter_id BIGINT,
                                   parameter_name VARCHAR(100) NOT NULL,
                                   unit VARCHAR(20) NOT NULL,
                                   volume INT NOT NULL,
                                   unit_price NUMERIC(10,2) NOT NULL,
                                   line_total NUMERIC(10,2) NOT NULL,

                                   CONSTRAINT fk_application_items_application
                                       FOREIGN KEY (application_id)
                                           REFERENCES applications(id)
                                           ON DELETE CASCADE,

                                   CONSTRAINT fk_application_items_service_parameter
                                       FOREIGN KEY (service_parameter_id)
                                           REFERENCES service_parameters(id)
                                           ON DELETE SET NULL,

                                   CONSTRAINT chk_application_items_volume
                                       CHECK (volume > 0),

                                   CONSTRAINT chk_application_items_unit_price
                                       CHECK (unit_price >= 0),

                                   CONSTRAINT chk_application_items_line_total
                                       CHECK (line_total >= 0)
);