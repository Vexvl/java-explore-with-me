create table if not exists endpoint_hit
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    app_name  VARCHAR                             NOT NULL,
    app_uri   VARCHAR                             NOT NULL,
    ip        VARCHAR                             NOT NULL,
    timestamp TIMESTAMP                           NOT NULL,
    constraint pk_endpoint_hit primary key (id)
);