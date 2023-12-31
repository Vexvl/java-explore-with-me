CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(250)                            NOT NULL,
    email VARCHAR(254)                            NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(50)                             NOT NULL,
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT qk_category_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title              VARCHAR(7000)                           NOT NULL,
    description        VARCHAR(7000)                           NOT NULL,
    annotation         VARCHAR(2000)                           NOT NULL,
    lat                FLOAT,
    lon                FLOAT,
    event_date         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    views              BIGINT,
    confirmed_request  BIGINT,
    request_moderation BOOLEAN,
    category           BIGINT,
    participant_limit  INTEGER                                 NOT NULL,
    state              VARCHAR(40),
    paid               BOOLEAN                                 NOT NULL,
    users              BIGINT                                  NOT NULL,
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT fk_event_user FOREIGN KEY (users) REFERENCES users (id),
    CONSTRAINT fk_event_category FOREIGN KEY (category) REFERENCES categories (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created        TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    event_id       BIGINT                                  NOT NULL,
    requester_id   BIGINT                                  NOT NULL,
    request_status VARCHAR                                 NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT fk_requests_requester FOREIGN KEY (requester_id) REFERENCES users (id),
    CONSTRAINT fk_requests_event FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS compilation
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title  VARCHAR(50)                             NOT NULL,
    pinned BOOLEAN                                 NOT NULL,
    CONSTRAINT pk_compilation PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilation_events
(
    id       BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    PRIMARY KEY (id, event_id),
    CONSTRAINT fk_compilation FOREIGN KEY (id) REFERENCES compilation (id),
    CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE IF NOT EXISTS liked
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id BIGINT                                  NOT NULL,
    user_id  BIGINT                                  NOT NULL,
    CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS disliked
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    event_id BIGINT                                  NOT NULL,
    user_id  BIGINT                                  NOT NULL,
    CONSTRAINT fk_event FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id)
);