CREATE TABLE accounts (
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id               BIGINT       NOT NULL REFERENCES users (id),
    name                  VARCHAR(100) NOT NULL,
    type                  VARCHAR(20)  NOT NULL,
    initial_balance_cents BIGINT       NOT NULL,
    archived              BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at            TIMESTAMPTZ  NOT NULL,
    updated_at            TIMESTAMPTZ  NOT NULL,
    CONSTRAINT uk_accounts_user_name UNIQUE (user_id, name),
    CONSTRAINT ck_accounts_type CHECK (type IN ('CHECKING', 'SAVINGS', 'WALLET'))
);
