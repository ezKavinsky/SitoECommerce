CREATE SCHEMA orders;

CREATE SEQUENCE user_seq;

CREATE TABLE "user" (
    id INTEGER DEFAULT NEXTVAL ('user_seq') PRIMARY KEY,
    code VARCHAR(70),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    telephone_number VARCHAR(20),
    email VARCHAR(90),
    address VARCHAR(150),
    birth_date DATE,
    registration_date DATE
);

CREATE SEQUENCE product_seq;

CREATE TABLE product (
    id INTEGER DEFAULT NEXTVAL ('product_seq') PRIMARY KEY,
    name VARCHAR(50),
    brand VARCHAR(50),
    bar_code VARCHAR(70),
    description VARCHAR(500),
    price FLOAT,
    quantity INTEGER,
    production_year INTEGER,
    free_shipping BOOLEAN,
    shipping_price FLOAT,
    score FLOAT,
    version VARCHAR(500)
);

CREATE SEQUENCE credit_card_seq;

CREATE TABLE credit_card (
    id INTEGER DEFAULT NEXTVAL ('credit_card_seq') PRIMARY KEY,
    number VARCHAR(30),
    expiration_month INTEGER,
    expiration_year INTEGER,
    security_code INTEGER,
    owner INTEGER,
    FOREIGN KEY (owner) REFERENCES "user"(id)
);

CREATE SEQUENCE purchase_seq;

CREATE TABLE purchase (
    id INTEGER DEFAULT NEXTVAL ('purchase_seq') PRIMARY KEY,
    buyer INTEGER,
    credit_card INTEGER,
    purchase_time DATE,
    total FLOAT,
    shipped BOOLEAN,
    FOREIGN KEY (buyer) REFERENCES "user" (id),
    FOREIGN KEY (credit_card) REFERENCES credit_card (id)
);

CREATE SEQUENCE product_in_purchase_seq;

CREATE TABLE product_in_purchase (
    id INTEGER DEFAULT NEXTVAL ('product_in_purchase_seq') PRIMARY KEY,
    related_purchase INTEGER,
    product INTEGER,
    quantity INTEGER,
    final_price FLOAT,
    FOREIGN KEY (related_purchase) REFERENCES purchase (id),
    FOREIGN KEY (product) REFERENCES product (id)
);

CREATE SEQUENCE review_seq;

CREATE TABLE review (
    id INTEGER DEFAULT NEXTVAL ('review_seq') PRIMARY KEY,
    title VARCHAR(50),
    comment VARCHAR(300),
    stars FLOAT,
    product INTEGER,
    buyer INTEGER,
    FOREIGN KEY (product) REFERENCES product(id),
    FOREIGN KEY (buyer) REFERENCES "user" (id)
);

CREATE SEQUENCE promo_seq;

CREATE TABLE promo (
    id INTEGER DEFAULT NEXTVAL ('promo_seq') PRIMARY KEY,
    name VARCHAR(50),
    discount INTEGER,
    start_date DATE,
    end_date DATE
);

CREATE SEQUENCE product_in_promo_seq;

CREATE TABLE product_in_promo (
    id INTEGER DEFAULT NEXTVAL ('product_in_promo_seq') PRIMARY KEY,
    related_promo INTEGER,
    product INTEGER,
    discount_price FLOAT,
    FOREIGN KEY (related_promo) REFERENCES promo(id),
    FOREIGN KEY (product) REFERENCES product(id)
);

CREATE SEQUENCE product_in_promo_purchase_seq;

CREATE TABLE product_in_promo_purchase (
    id INTEGER DEFAULT NEXTVAL ('product_in_promo_purchase_seq') PRIMARY KEY,
    related_purchase INTEGER,
    product_in_promo INTEGER,
    quantity INTEGER,
    final_price FLOAT,
    FOREIGN KEY (related_purchase) REFERENCES purchase (id),
    FOREIGN KEY (product_in_promo) REFERENCES product_in_promo (id)
);

CREATE SEQUENCE cart_seq;

CREATE TABLE cart(
    id INTEGER DEFAULT NEXTVAL('cart_seq') PRIMARY KEY,
    buyer INTEGER,
    total FLOAT,
    FOREIGN KEY (buyer) REFERENCES "user"(id)
);
