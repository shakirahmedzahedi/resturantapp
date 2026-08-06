INSERT INTO order_positions (position_code, position_name) VALUES
('COUNTER-1', 'Counter 1'),
('COUNTER-2', 'Counter 2'),
('COUNTER-3', 'Counter 3'),
('COUNTER-4', 'Counter 4'),
('COUNTER-5', 'Counter 5'),
('COUNTER-6', 'Counter 6');

INSERT INTO products (product_code, name_en, name_bn, price, display_order) VALUES
('P001', 'Muri', 'মুড়ি', 0, 1),
('P002', 'Chatpati / Fuchka', 'চটপটি / ফুচকা', 0, 2),
('P003', 'Singara', 'সিঙ্গারা', 0, 3),
('P004', 'Roll', 'রোল', 0, 4),
('P005', 'Pudding', 'পুডিং', 0, 5),
('P006', 'Haleem', 'হালিম', 0, 6),
('P007', 'Tehari', 'তেহারি', 0, 7),
('P008', 'Aam / Jambura Bhorta', 'আম / জাম্বুরা ভর্তা', 0, 8),
('P009', 'Cake', 'কেক', 0, 9),
('P010', 'Chitoi', 'চিতই পিঠা', 0, 10);

INSERT INTO app_users (username, password_hash, display_name, role) VALUES
('Tusher', '$2a$10$5jvP1H1yP6TqR0fBD9pIouYt3jWRYI/I5EcaXVh0onPSTZREJg5bS', 'Tusher', 'ORDER_TAKER'),
('Eity', '$2a$10$5jvP1H1yP6TqR0fBD9pIouYt3jWRYI/I5EcaXVh0onPSTZREJg5bS', 'Eity', 'ORDER_TAKER'),
('Mary', '$2a$10$5jvP1H1yP6TqR0fBD9pIouYt3jWRYI/I5EcaXVh0onPSTZREJg5bS', 'Mary', 'ORDER_TAKER'),
('Istiak', '$2a$10$5jvP1H1yP6TqR0fBD9pIouYt3jWRYI/I5EcaXVh0onPSTZREJg5bS', 'Istiak', 'ORDER_TAKER'),
('Farzana', '$2a$10$5jvP1H1yP6TqR0fBD9pIouYt3jWRYI/I5EcaXVh0onPSTZREJg5bS', 'Farzana', 'ORDER_TAKER'),
('Camelia', '$2a$10$5jvP1H1yP6TqR0fBD9pIouYt3jWRYI/I5EcaXVh0onPSTZREJg5bS', 'Camelia', 'ORDER_TAKER'),
('kitchen1', '$2a$10$5jvP1H1yP6TqR0fBD9pIouYt3jWRYI/I5EcaXVh0onPSTZREJg5bS', 'kitchen1', 'KITCHEN'),
('kitchen2',  '$2a$10$5jvP1H1yP6TqR0fBD9pIouYt3jWRYI/I5EcaXVh0onPSTZREJg5bS', 'kitchen2',   'KITCHEN'),
('admin',    '$2a$10$5jvP1H1yP6TqR0fBD9pIouYt3jWRYI/I5EcaXVh0onPSTZREJg5bS', 'Admin',     'ADMIN');
