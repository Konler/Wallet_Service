CREATE SCHEMA my_schema;
CREATE SCHEMA service;

-- ChangeSet create-players-table
-- Author: Konler
-- Table: players

CREATE TABLE my_schema.players (
                                   id BIGSERIAL PRIMARY KEY,
                                   login VARCHAR(255) NOT NULL UNIQUE,
                                   balance DOUBLE PRECISION NOT NULL,
                                   password INT NOT NULL
);

-- ChangeSet create-transactions-table
-- Author: Konler
-- Table: transactions

CREATE TABLE my_schema.transactions (
                                        id BIGSERIAL PRIMARY KEY,
                                        type VARCHAR(255) NOT NULL,
                                        amount DOUBLE PRECISION NOT NULL,
                                        state VARCHAR(255) NOT NULL,
                                        player_id BIGINT NOT NULL
);

-- ChangeSet create-history-table
-- Author: Konler
-- Table: history

CREATE TABLE my_schema.history (
                                   id BIGSERIAL PRIMARY KEY,
                                   player_id BIGINT NOT NULL,
                                   action VARCHAR(255) NOT NULL,
                                   time TIMESTAMP NOT NULL
);

-- ChangeSet create-liquibase-service-table
-- Author: Konler
-- Table: DATABASECHANGELOG

CREATE TABLE service.DATABASECHANGELOG (
    ID INT PRIMARY KEY NOT NULL
);
