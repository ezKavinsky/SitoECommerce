CREATE SCHEMA orders;

CREATE SEQUENCE user_seq;

CREATE TABLE "user" (
    id INTEGER DEFAULT NEXTVAL ('user_seq') PRIMARY KEY,
    code VARCHAR(70),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    telephone_number VARCHAR(20),
    email VARCHAR(90),
    address VARCHAR(150)
);

CREATE SEQUENCE product_seq;

CREATE TABLE product (
    id INTEGER DEFAULT NEXTVAL ('product_seq') PRIMARY KEY,
    name VARCHAR(50),
    brand VARCHAR(50),
    bar_code VARCHAR(70),
    description VARCHAR(500),
    price FLOAT,
    quantity FLOAT,
    production_year INTEGER,
    inPromo BOOLEAN,
    freeShipping BOOLEAN,
    score FLOAT,
    version VARCHAR(500)
);

CREATE SEQUENCE purchase_seq;

CREATE TABLE purchase (
    id INTEGER DEFAULT NEXTVAL ('purchase_seq') PRIMARY KEY,
    buyer INTEGER,
    purchase_time TIMESTAMP(0) DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (buyer) REFERENCES "user" (id)
);

CREATE SEQUENCE product_in_purchase_seq;

CREATE TABLE product_in_purchase (
    id INTEGER DEFAULT NEXTVAL ('product_in_purchase_seq') PRIMARY KEY,
    related_purchase INTEGER,
    product INTEGER,
    quantity INTEGER,
    FOREIGN KEY (related_purchase) REFERENCES purchase (id),
    FOREIGN KEY (product) REFERENCES product (id)
);

CREATE SEQUENCE review_seq;

CREATE TABLE review (
    id INTEGER DEFAULT NEXTVAL ('review_seq') PRIMARY KEY,
    title VARCHAR(50),
    stars FLOAT,
    product INTEGER,
    "user" INTEGER,
    FOREIGN KEY (product) REFERENCES product(id),
    FOREIGN KEY ("user") REFERENCES "user" (id)
);

CREATE SEQUENCE credit_card_seq;

CREATE TABLE credit_card (
    id INTEGER DEFAULT NEXTVAL ('credit_card_seq') PRIMARY KEY,
    number INTEGER,
    expiration_date DATE,
    security_code INTEGER,
    "user" INTEGER,
    FOREIGN KEY ("user") REFERENCES "user"(id)
);