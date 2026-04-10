CREATE TABLE tariffs (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(100) NOT NULL UNIQUE,
                         description TEXT,
                         is_active BOOLEAN DEFAULT TRUE,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);