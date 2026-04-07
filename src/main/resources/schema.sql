CREATE TABLE IF NOT EXISTS brands (
    id   BIGINT      NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_brands PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS prices (
    id         BIGINT         NOT NULL AUTO_INCREMENT,
    brand_id   BIGINT         NOT NULL,
    start_date TIMESTAMP      NOT NULL,
    end_date   TIMESTAMP      NOT NULL,
    price_list BIGINT         NOT NULL,
    product_id BIGINT         NOT NULL,
    priority   INTEGER        NOT NULL,
    price      DOUBLE         NOT NULL,
    currency   VARCHAR(3)     NOT NULL,
    CONSTRAINT pk_prices     PRIMARY KEY (id),
    CONSTRAINT fk_prices_brand FOREIGN KEY (brand_id) REFERENCES brands (id)
);
