DROP TABLE IF EXISTS comanda_produs CASCADE;
DROP TABLE IF EXISTS comenzi CASCADE;
DROP TABLE IF EXISTS produse CASCADE;
DROP TABLE IF EXISTS restaurante CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Tabelul pentru clasele care extind abstractizarea 'User' (Admin, Client, Curier)
-- Un singur tabel si o coloana 'tip_user'
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    telefon VARCHAR(20),
    email VARCHAR(100) UNIQUE NOT NULL,
    parola VARCHAR(255) NOT NULL,
    tip_user VARCHAR(50) NOT NULL, -- 'Admin', 'Client', 'Curier'
    este_disponibil BOOLEAN DEFAULT true -- folosit doar pentru Curier
);

-- 2. Tabelul pentru 'Restaurant'
CREATE TABLE restaurante (
    id SERIAL PRIMARY KEY,
    nume VARCHAR(100) NOT NULL,
    specific VARCHAR(100),
    rating DECIMAL(3, 2) DEFAULT 0.0,
    numar_recenzii INTEGER DEFAULT 0
);

-- 3. Tabelul pentru clasele care extind abstractizarea 'Produs' (Mancare, Bautura)
-- Un singur tabel si o coloana 'tip_produs'
CREATE TABLE produse (
    id SERIAL PRIMARY KEY,
    restaurant_id INTEGER NOT NULL REFERENCES restaurante(id) ON DELETE CASCADE,
    nume VARCHAR(100) NOT NULL,
    descriere TEXT,
    pret DECIMAL(10, 2) NOT NULL,
    calorii INTEGER,
    tip_produs VARCHAR(50) NOT NULL 
);

-- 4. Tabelul pentru 'Comanda'
CREATE TABLE comenzi (
    id SERIAL PRIMARY KEY,
    client_id INTEGER NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    curier_id INTEGER REFERENCES users(id) ON DELETE SET NULL,
    restaurant_id INTEGER NOT NULL REFERENCES restaurante(id) ON DELETE RESTRICT,
    adresa_oras VARCHAR(100),
    adresa_strada VARCHAR(150),
    adresa_numar INTEGER,
    status VARCHAR(50) DEFAULT 'IN_ASTEPTARE', -- 'IN_ASTEPTARE', 'IN_LIVRARE', 'FINALIZAT'
    data_plasarii TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    pret_total DECIMAL(10, 2) DEFAULT 0.0
);

-- 5. Tabelul de legatura pentru Map<Produs, Integer> din clasa Comanda
CREATE TABLE comanda_produs (
    comanda_id INTEGER REFERENCES comenzi(id) ON DELETE CASCADE,
    produs_id INTEGER REFERENCES produse(id) ON DELETE RESTRICT,
    cantitate INTEGER NOT NULL DEFAULT 1,
    PRIMARY KEY (comanda_id, produs_id)
);
