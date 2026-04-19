INSERT INTO users (
    id,
    type,
    name,
    email,
    login,
    password,
    address_street,
    address_number,
    address_city,
    address_zip_code,
    last_updated_at
) VALUES
(
    'a1f5c3d8-9e2b-4f0a-8b9c-1234567890ab',
    'CUSTOMER',
    'João Silva',
    'joao@email.com',
    'joaoCustomer',
    '$2a$10$/FnjWxNEUhUMNUcVdXUc7OtewxMWGrMaULxTqZ8Mktt1Co5SUlPjS',
    'Rua das Flores',
    '123',
    'São Paulo',
    '12345678',
    CURRENT_TIMESTAMP
),
(
    'b2d6e4f1-1c3d-4a2b-9f0d-2345678901bc',
    'CUSTOMER',
    'Maria Souza',
    'maria@email.com',
    'mariaCustomer',
    '$2a$10$/FnjWxNEUhUMNUcVdXUc7OtewxMWGrMaULxTqZ8Mktt1Co5SUlPjS',
    'Av Paulista',
    '1000',
    'São Paulo',
    '87654321',
    CURRENT_TIMESTAMP
),

(
    'c3e7f5a2-2d4e-5b3c-0a1e-3456789012cd',
    'RESTAURANT_OWNER',
    'Carlos Oliveira',
    'carlos@restaurant.com',
    'carlosOwner',
    '$2a$10$/FnjWxNEUhUMNUcVdXUc7OtewxMWGrMaULxTqZ8Mktt1Co5SUlPjS',
    'Rua Gourmet',
    '45',
    'Rio de Janeiro',
    '22222222',
    CURRENT_TIMESTAMP
),
(
    'd4f8a6b3-3e5f-6c4d-1b2f-4567890123de',
    'RESTAURANT_OWNER',
    'Ana Pereira',
    'ana@restaurant.com',
    'anaOwner',
    '$2a$10$/FnjWxNEUhUMNUcVdXUc7OtewxMWGrMaULxTqZ8Mktt1Co5SUlPjS',
    'Rua dos Chefs',
    '77',
    'Curitiba',
    '33333333',
    CURRENT_TIMESTAMP
);