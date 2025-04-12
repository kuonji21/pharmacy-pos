-- Fix user accounts to avoid duplicate usernames
DELETE FROM user WHERE id IN (1, 2, 3);

INSERT INTO user (id, username, password, name, position) VALUES
(1, 'admin', 'admin', 'Admin', 'admin');

INSERT INTO user (id, username, password, name, position) VALUES
(2, 'cashier', 'cashier', 'Cashier Pharmacy', 'Cashier');

INSERT INTO user (id, username, password, name, position) VALUES
(3, 'admin2', 'admin123', 'Administrator', 'admin');