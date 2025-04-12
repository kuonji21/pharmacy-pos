-- Oracle compatible SQL schema for Pharmacy POS system
-- Use this script in SQL Developer with Oracle XE 21.3

-- Create sequences for auto-incrementing fields
CREATE SEQUENCE collection_seq START WITH 18 INCREMENT BY 1;
CREATE SEQUENCE customer_seq START WITH 15 INCREMENT BY 1;
CREATE SEQUENCE products_seq START WITH 58 INCREMENT BY 1;
CREATE SEQUENCE purchases_seq START WITH 14 INCREMENT BY 1;
CREATE SEQUENCE purchases_item_seq START WITH 12 INCREMENT BY 1;
CREATE SEQUENCE sales_seq START WITH 142 INCREMENT BY 1;
CREATE SEQUENCE sales_order_seq START WITH 315 INCREMENT BY 1;
CREATE SEQUENCE supliers_seq START WITH 5 INCREMENT BY 1;
CREATE SEQUENCE user_seq START WITH 4 INCREMENT BY 1;

-- Table: collection
CREATE TABLE "COLLECTION" (
  transaction_id NUMBER(11) PRIMARY KEY,
  date VARCHAR2(100) NOT NULL,
  name VARCHAR2(100) NOT NULL,
  invoice VARCHAR2(100) NOT NULL,
  amount VARCHAR2(100) NOT NULL,
  remarks VARCHAR2(100) NOT NULL,
  balance NUMBER(11) NOT NULL
);

-- Auto-increment trigger for collection
CREATE OR REPLACE TRIGGER collection_tr
BEFORE INSERT ON "COLLECTION"
FOR EACH ROW
BEGIN
  SELECT collection_seq.NEXTVAL INTO :NEW.transaction_id FROM dual;
END;
/

-- Table: customer
CREATE TABLE "CUSTOMER" (
  customer_id NUMBER(11) PRIMARY KEY,
  customer_name VARCHAR2(100) NOT NULL,
  address VARCHAR2(100) NOT NULL,
  contact VARCHAR2(100) NOT NULL,
  membership_number VARCHAR2(100) NOT NULL,
  prod_name VARCHAR2(550) NOT NULL,
  expected_date VARCHAR2(500) NOT NULL,
  note VARCHAR2(500) NOT NULL
);

-- Auto-increment trigger for customer
CREATE OR REPLACE TRIGGER customer_tr
BEFORE INSERT ON "CUSTOMER"
FOR EACH ROW
BEGIN
  SELECT customer_seq.NEXTVAL INTO :NEW.customer_id FROM dual;
END;
/

-- Table: products
CREATE TABLE "PRODUCTS" (
  product_id NUMBER(11) PRIMARY KEY,
  product_code VARCHAR2(200) NOT NULL,
  gen_name VARCHAR2(200) NOT NULL,
  product_name VARCHAR2(200) NOT NULL,
  cost VARCHAR2(100) NOT NULL,
  o_price VARCHAR2(100) NOT NULL,
  price VARCHAR2(100) NOT NULL,
  profit VARCHAR2(100) NOT NULL,
  supplier VARCHAR2(100) NOT NULL,
  onhand_qty NUMBER(10) NOT NULL,
  qty NUMBER(10) NOT NULL,
  qty_sold NUMBER(10) NOT NULL,
  expiry_date VARCHAR2(500) NOT NULL,
  date_arrival VARCHAR2(500) NOT NULL
);

-- Auto-increment trigger for products
CREATE OR REPLACE TRIGGER products_tr
BEFORE INSERT ON "PRODUCTS"
FOR EACH ROW
BEGIN
  SELECT products_seq.NEXTVAL INTO :NEW.product_id FROM dual;
END;
/

-- Table: purchases
CREATE TABLE "PURCHASES" (
  transaction_id NUMBER(11) PRIMARY KEY,
  invoice_number VARCHAR2(100) NOT NULL,
  date VARCHAR2(100) NOT NULL,
  suplier VARCHAR2(100) NOT NULL,
  remarks VARCHAR2(100) NOT NULL
);

-- Auto-increment trigger for purchases
CREATE OR REPLACE TRIGGER purchases_tr
BEFORE INSERT ON "PURCHASES"
FOR EACH ROW
BEGIN
  SELECT purchases_seq.NEXTVAL INTO :NEW.transaction_id FROM dual;
END;
/

-- Table: purchases_item
CREATE TABLE "PURCHASES_ITEM" (
  id NUMBER(11) PRIMARY KEY,
  name VARCHAR2(100) NOT NULL,
  qty NUMBER(11) NOT NULL,
  cost VARCHAR2(100) NOT NULL,
  invoice VARCHAR2(100) NOT NULL
);

-- Auto-increment trigger for purchases_item
CREATE OR REPLACE TRIGGER purchases_item_tr
BEFORE INSERT ON "PURCHASES_ITEM"
FOR EACH ROW
BEGIN
  SELECT purchases_item_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/

-- Table: sales
CREATE TABLE "SALES" (
  transaction_id NUMBER(11) PRIMARY KEY,
  invoice_number VARCHAR2(100) NOT NULL,
  cashier VARCHAR2(100) NOT NULL,
  date VARCHAR2(100) NOT NULL,
  type VARCHAR2(100) NOT NULL,
  amount VARCHAR2(100) NOT NULL,
  profit VARCHAR2(100) NOT NULL,
  due_date VARCHAR2(100) NOT NULL,
  name VARCHAR2(100) NOT NULL,
  balance VARCHAR2(100) NOT NULL
);

-- Auto-increment trigger for sales
CREATE OR REPLACE TRIGGER sales_tr
BEFORE INSERT ON "SALES"
FOR EACH ROW
BEGIN
  SELECT sales_seq.NEXTVAL INTO :NEW.transaction_id FROM dual;
END;
/

-- Table: sales_order
CREATE TABLE "SALES_ORDER" (
  transaction_id NUMBER(11) PRIMARY KEY,
  invoice VARCHAR2(100) NOT NULL,
  product VARCHAR2(100) NOT NULL,
  qty VARCHAR2(100) NOT NULL,
  amount VARCHAR2(100) NOT NULL,
  profit VARCHAR2(100) NOT NULL,
  product_code VARCHAR2(150) NOT NULL,
  gen_name VARCHAR2(200) NOT NULL,
  name VARCHAR2(200) NOT NULL,
  price VARCHAR2(100) NOT NULL,
  discount VARCHAR2(100) NOT NULL,
  date VARCHAR2(500) NOT NULL
);

-- Auto-increment trigger for sales_order
CREATE OR REPLACE TRIGGER sales_order_tr
BEFORE INSERT ON "SALES_ORDER"
FOR EACH ROW
BEGIN
  SELECT sales_order_seq.NEXTVAL INTO :NEW.transaction_id FROM dual;
END;
/

-- Table: supliers
CREATE TABLE "SUPLIERS" (
  suplier_id NUMBER(11) PRIMARY KEY,
  suplier_name VARCHAR2(100) NOT NULL,
  suplier_address VARCHAR2(100) NOT NULL,
  suplier_contact VARCHAR2(100) NOT NULL,
  contact_person VARCHAR2(100) NOT NULL,
  note VARCHAR2(500) NOT NULL
);

-- Auto-increment trigger for supliers
CREATE OR REPLACE TRIGGER supliers_tr
BEFORE INSERT ON "SUPLIERS"
FOR EACH ROW
BEGIN
  SELECT supliers_seq.NEXTVAL INTO :NEW.suplier_id FROM dual;
END;
/

-- Table: user (note: USER is a reserved word in Oracle)
CREATE TABLE "USER" (
  id NUMBER(11) PRIMARY KEY,
  username VARCHAR2(100) NOT NULL,
  password VARCHAR2(100) NOT NULL,
  name VARCHAR2(100) NOT NULL,
  position VARCHAR2(100) NOT NULL
);

-- Auto-increment trigger for user
CREATE OR REPLACE TRIGGER user_tr
BEFORE INSERT ON "USER"
FOR EACH ROW
BEGIN
  SELECT user_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/

-- Insert initial user data
INSERT INTO "USER" (id, username, password, name, position) VALUES
(1, 'admin', 'admin', 'Admin', 'admin');

INSERT INTO "USER" (id, username, password, name, position) VALUES
(2, 'cashier', 'cashier', 'Cashier Pharmacy', 'Cashier');

INSERT INTO "USER" (id, username, password, name, position) VALUES
(3, 'admin', 'admin123', 'Administrator', 'admin');

-- Commit the changes
COMMIT;

-- Display a success message
SELECT 'Oracle schema creation completed successfully' FROM dual;