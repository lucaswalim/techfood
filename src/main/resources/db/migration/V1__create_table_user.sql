CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    type VARCHAR(30) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    address_street VARCHAR(255),
    address_number VARCHAR(20),
    address_city VARCHAR(100),
    address_zip_code VARCHAR(8),
    last_updated_at TIMESTAMP NOT NULL
);
