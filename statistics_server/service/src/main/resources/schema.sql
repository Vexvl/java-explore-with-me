CREATE TABLE IF NOT EXISTS endpoint_hit
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    app       VARCHAR                             NOT NULL,
    uri       VARCHAR                             NOT NULL,
    ip        VARCHAR                             NOT NULL,
    timestamp TIMESTAMP                           NOT NULL,
    CONSTRAINT pk_endpoint_hit PRIMARY KEY (id)
);