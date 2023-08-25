CREATE TABLE IF NOT EXISTS endpoint_hit
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    app       VARCHAR(512)                        NOT NULL,
    uri       VARCHAR(512)                        NOT NULL,
    ip        VARCHAR(32)                         NOT NULL,
    timestamp TIMESTAMP                           NOT NULL,
    CONSTRAINT pk_endpoint_hit PRIMARY KEY (id)
);