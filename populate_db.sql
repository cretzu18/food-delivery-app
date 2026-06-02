TRUNCATE TABLE comanda_produs, comenzi, produse, restaurante, users RESTART IDENTITY CASCADE;

-- Populare Users
INSERT INTO users (nume, telefon, email, parola, tip_user, adresa_oras, adresa_strada, adresa_numar, tip_vehicul, este_disponibil) VALUES
('Admin Sef', '0700000000', 'admin@app.ro', 'admin', 'Admin', NULL, NULL, NULL, NULL, NULL),
('Ion Popescu', '0711111111', 'ion@app.ro', 'client', 'Client', 'Bucuresti', 'Eroilor', 10, NULL, NULL),
('Maria Ionescu', '0711222333', 'maria@app.ro', 'client', 'Client', 'Bucuresti', 'Unirii', 25, NULL, NULL),
('Vasile Curierul', '0722222222', 'vasile@app.ro', 'curier', 'Curier', NULL, NULL, NULL, 'Bicicleta', TRUE),
('Mihai Livratorul', '0733333333', 'mihai@app.ro', 'curier', 'Curier', NULL, NULL, NULL, 'Scuter', FALSE);

-- Populare Restaurante
INSERT INTO restaurante (nume, specific, rating, numar_recenzii) VALUES
('Burger King', 'Fast Food', 4.5, 10),
('La Mama', 'Traditional Romanesc', 4.8, 25),
('Pizza Hut', 'Italian', 4.2, 18);

-- Populare Produse (Mancare + Bautura)
INSERT INTO produse (restaurant_id, nume, descriere, pret, calorii, tip_produs, mancare_gramaj, bautura_volum, bautura_contine_alcool) VALUES
-- Produse pentru Burger King (id: 1)
(1, 'Cheeseburger', 'Burger de vita cu branza cheddar, salata si sos', 25.0, 450, 'Mancare', 250, NULL, NULL),
(1, 'Cartofi Prajiti', 'Portie mare de cartofi prajiti cu sare', 12.0, 300, 'Mancare', 150, NULL, NULL),
(1, 'Coca Cola', 'Doza de bautura carbogazoasa', 8.0, 140, 'Bautura', NULL, 330, FALSE),
(1, 'Apa Minerala', 'Apa minerala naturala carbogazoasa', 6.0, 0, 'Bautura', NULL, 500, FALSE),

-- Produse pentru La Mama (id: 2)
(2, 'Sarmale cu mamaliguta', '4 sarmale din carne de porc, servite cu mamaliguta si ardei iute', 35.0, 600, 'Mancare', 450, NULL, NULL),
(2, 'Ciorba de burta', 'Ciorba traditionala, servita cu smantana si paine', 22.0, 350, 'Mancare', 400, NULL, NULL),
(2, 'Bere Ursus', 'Bere blonda la sticla', 10.0, 150, 'Bautura', NULL, 500, TRUE),

-- Produse pentru Pizza Hut (id: 3)
(3, 'Pizza Margherita', 'Pizza cu sos de rosii, mozzarella si busuioc', 30.0, 800, 'Mancare', 450, NULL, NULL);

-- Adaugam o comanda de test pentru Ion Popescu catre Burger King, livrata de Vasile
INSERT INTO comenzi (client_id, curier_id, restaurant_id, pret_total, status) VALUES
(2, 4, 1, 45.0, 'In Curs');

-- Adaugam produsele comenzii in `comanda_produs`
-- Comanda 1 contine: 1 Cheeseburger, 1 Cartofi, 1 Cola
INSERT INTO comanda_produs (comanda_id, produs_id, cantitate) VALUES
(1, 1, 1),
(1, 2, 1),
(1, 6, 1);
