-- ============ USERS ============

CREATE TABLE IF NOT EXISTS users (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255),      -- ФИО одной строкой
    middlename      VARCHAR(255),
    lastname        VARCHAR(255),
    login           VARCHAR(100) NOT NULL UNIQUE,
    password        VARCHAR(255) NOT NULL,
    is_work         BOOLEAN      NOT NULL DEFAULT TRUE,
    companyid       BIGINT,
    parentuserid    BIGINT
);

ALTER TABLE users
    ADD CONSTRAINT fk_users_parent
    FOREIGN KEY (parentuserid) REFERENCES users(id)
    ON DELETE SET NULL;


-- ============ ACTIVE_TYPES ============

CREATE TABLE IF NOT EXISTS active_types (
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL UNIQUE,
    is_soft          BOOLEAN      NOT NULL DEFAULT FALSE,
    number_template  VARCHAR(255)
);


-- ============ STATUSES ============

CREATE TABLE IF NOT EXISTS statuses (
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    is_soft  BOOLEAN      NOT NULL DEFAULT FALSE
);


-- ============ ACTIVES ============

CREATE TABLE IF NOT EXISTS actives (
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    typeid           BIGINT       NOT NULL,
    description      TEXT,
    parentactiveid   BIGINT,
    uniquenumber     VARCHAR(100) NOT NULL UNIQUE,
    statusid         BIGINT       NOT NULL,
    serialnumber     VARCHAR(100),
    bitrixcode       VARCHAR(100),
    onescode         VARCHAR(100),
    address          VARCHAR(500),
    date_create      TIMESTAMP    NOT NULL DEFAULT NOW()
);

ALTER TABLE actives
    ADD CONSTRAINT fk_actives_type
    FOREIGN KEY (typeid) REFERENCES active_types(id);

ALTER TABLE actives
    ADD CONSTRAINT fk_actives_status
    FOREIGN KEY (statusid) REFERENCES statuses(id);

ALTER TABLE actives
    ADD CONSTRAINT fk_actives_parent
    FOREIGN KEY (parentactiveid) REFERENCES actives(id)
    ON DELETE SET NULL;


-- ============ USERS_ACTIVES ============

CREATE TABLE IF NOT EXISTS users_actives (
    id          BIGSERIAL PRIMARY KEY,
    activeid    BIGINT NOT NULL,
    userid      BIGINT NOT NULL,
    isapproved  BOOLEAN NOT NULL DEFAULT TRUE
);

ALTER TABLE users_actives
    ADD CONSTRAINT fk_users_actives_active
    FOREIGN KEY (activeid) REFERENCES actives(id)
    ON DELETE CASCADE;

ALTER TABLE users_actives
    ADD CONSTRAINT fk_users_actives_user
    FOREIGN KEY (userid) REFERENCES users(id)
    ON DELETE CASCADE;


-- ============ ACTIVE_ATTRIBUTES ============

CREATE TABLE IF NOT EXISTS active_attributes (
    id        BIGSERIAL PRIMARY KEY,
    name      VARCHAR(255) NOT NULL,
    activeid  BIGINT NOT NULL,
    typeid    BIGINT
);

ALTER TABLE active_attributes
    ADD CONSTRAINT fk_active_attributes_active
    FOREIGN KEY (activeid) REFERENCES actives(id)
    ON DELETE CASCADE;

ALTER TABLE active_attributes
    ADD CONSTRAINT fk_active_attributes_type
    FOREIGN KEY (typeid) REFERENCES active_types(id)
    ON DELETE SET NULL;


-- ============ COMMENTS ============

CREATE TABLE IF NOT EXISTS comments (
    id         BIGSERIAL PRIMARY KEY,
    activeid   BIGINT NOT NULL,
    comment    TEXT   NOT NULL,
    isprivat   BOOLEAN NOT NULL DEFAULT FALSE
);

ALTER TABLE comments
    ADD CONSTRAINT fk_comments_active
    FOREIGN KEY (activeid) REFERENCES actives(id)
    ON DELETE CASCADE;
