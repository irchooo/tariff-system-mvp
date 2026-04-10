CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       keycloak_id UUID NOT NULL UNIQUE,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       phone_number VARCHAR(20) NOT NULL UNIQUE,
                       status VARCHAR(20) NOT NULL DEFAULT 'NEW',
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT chk_users_status
                           CHECK (status IN ('NEW', 'ACTIVE', 'ARCHIVED'))
);