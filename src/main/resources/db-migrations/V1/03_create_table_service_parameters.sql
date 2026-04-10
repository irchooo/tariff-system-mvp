CREATE TABLE service_parameters (
                                    id BIGSERIAL PRIMARY KEY,
                                    name VARCHAR(100) NOT NULL UNIQUE,
                                    unit VARCHAR(20) NOT NULL,
                                    price_per_unit NUMERIC(10,2) NOT NULL,
                                    is_active BOOLEAN DEFAULT TRUE,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                    CONSTRAINT chk_service_parameters_price
                                        CHECK (price_per_unit >= 0)
);