CREATE TABLE applications (
                              id BIGSERIAL PRIMARY KEY,
                              user_id BIGINT NOT NULL,
                              type VARCHAR(20) NOT NULL,
                              tariff_id BIGINT,
                              status VARCHAR(20) NOT NULL DEFAULT 'CREATED',
                              total_cost NUMERIC(10,2) NOT NULL,
                              contract_url TEXT,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT fk_applications_user
                                  FOREIGN KEY (user_id)
                                      REFERENCES users(id)
                                      ON DELETE RESTRICT,

                              CONSTRAINT fk_applications_tariff
                                  FOREIGN KEY (tariff_id)
                                      REFERENCES tariffs(id)
                                      ON DELETE RESTRICT,

                              CONSTRAINT chk_applications_type
                                  CHECK (type IN ('READY_TARIFF', 'CUSTOM')),

                              CONSTRAINT chk_applications_status
                                  CHECK (status IN ('CREATED', 'PAID', 'CONNECTING', 'COMPLETED', 'REJECTED', 'CANCELLED')),

                              CONSTRAINT chk_applications_total_cost
                                  CHECK (total_cost >= 0),

                              CONSTRAINT chk_applications_type_tariff
                                  CHECK (
                                      (type = 'READY_TARIFF' AND tariff_id IS NOT NULL)
                                          OR
                                      (type = 'CUSTOM' AND tariff_id IS NULL)
                                      )
);