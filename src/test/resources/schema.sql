CREATE TABLE IF NOT EXISTS product
(
    id         INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    product_name       VARCHAR(128)  NOT NULL,
    category           VARCHAR(32)  NOT NULL,
    price              INT          NOT NULL,
    stock              INT          NOT NULL
);