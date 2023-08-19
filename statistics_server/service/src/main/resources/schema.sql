CREATE TABLE IF NOT EXISTS endpoint
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name         VARCHAR(512)                            NOT NULL,
    uri          VARCHAR(512)                            NOT NULL,
    ip           VARCHAR(15)                             NOT NULL,
    request_time TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    CONSTRAINT pk_endpoint PRIMARY KEY (id)
);