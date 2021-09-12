CREATE TABLE oauth2_registered_client (
    id varchar(100) NOT NULL,
    client_id varchar(100) NOT NULL,
    client_id_issued_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_secret varchar(200) DEFAULT NULL,
    client_secret_expires_at timestamp DEFAULT NULL,
    client_name varchar(200) NOT NULL,
    client_authentication_methods varchar(1000) NOT NULL,
    authorization_grant_types varchar(1000) NOT NULL,
    redirect_uris varchar(1000) DEFAULT NULL,
    scopes varchar(1000) NOT NULL,
    client_settings varchar(2000) NOT NULL,
    token_settings varchar(2000) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE transaction (
    id IDENTITY NOT NULL,
    uuid VARCHAR(256) NOT NULL,
    description VARCHAR(1000),
    status VARCHAR(100) NOT NULL,
    amount DECIMAL(20,2) NOT NULL,
    currency CHAR(3) NOT NULL,
    date_created TIMESTAMP NOT NULL,
    merchant_uuid VARCHAR(256) NOT NULL,
    merchant_name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);