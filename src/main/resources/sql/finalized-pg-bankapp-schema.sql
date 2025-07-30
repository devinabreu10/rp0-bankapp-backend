-- Improved PostgreSQL schema for RP0 Banking Platform
-- With enhanced transaction table design and proper indexing

-- Create schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS "rp0-bankapp";

-- Customers table
create table "rp0-bankapp".customers
( --added trigger to auto increment customer_id
    customer_id SERIAL primary key,
    first_name  varchar(50)        not null,
    last_name   varchar(50)        not null,
    address     varchar(50)        not null,
    username    varchar(50) unique not null,
    passwrd     varchar(100)       not null
);

-- Create index on username for faster lookups
CREATE INDEX idx_customers_username ON "rp0-bankapp".customers (username);

-- Accounts table
create table "rp0-bankapp".accounts
(
    account_number  SERIAL primary key, --account number is the unique key
    nickname        varchar(50),
    account_type    varchar(20)    not null,
    account_balance decimal(15, 2) not null,
    created_at      timestamp      not null default (now() at time zone 'America/Chicago'),
    updated_at      timestamp      not null default (now() at time zone 'America/Chicago'),
    is_active		boolean        not null default true,
    customer_id     integer references "rp0-bankapp".customers
);

-- Create index on customer_id for faster account lookups by customer
CREATE INDEX idx_accounts_customer_id ON "rp0-bankapp".accounts (customer_id);

-- Transactions table (V1)
create table "rp0-bankapp".transactions
(
    transaction_id     SERIAL primary key,
    transaction_type   varchar(20)    not null,
    transaction_amount decimal(15, 2) not null,
    transaction_notes  varchar(100)   not null,
    created_at         timestamp      not null default (now() at time zone 'America/Chicago'),
    account_number     bigint references "rp0-bankapp".accounts
);
-- Create index on transaction_id for faster account lookups by transaction
CREATE INDEX idx_transactions_id ON "rp0-bankapp".transactions (transaction_id);

-- Loans table (Not yet implemented)
CREATE TABLE "rp0-bankapp".loans
(
    loan_id     BIGSERIAL PRIMARY KEY,
    loan_amount NUMERIC(15, 2) NOT NULL,
    loan_length INTEGER        NOT NULL,
    interest    NUMERIC(15, 2) NOT NULL,
    minimum_due NUMERIC(15, 2),
    due_date    DATE           NOT NULL,
    customer_id BIGINT         NOT NULL,
    CONSTRAINT customer_loan
        FOREIGN KEY (customer_id)
            REFERENCES "rp0-bankapp".customers (customer_id)
);
-- Create index on customer_id for faster loan lookups by customer
CREATE INDEX idx_loans_customer_id ON "rp0-bankapp".loans (customer_id);

-- Credit cards table (Not yet implemented)
CREATE TABLE "rp0-bankapp".credit_cards
(
    credit_id      BIGSERIAL PRIMARY KEY,
    credit_balance NUMERIC(15, 2) NOT NULL,
    minimum_due    NUMERIC(15, 2),
    credit_limit   NUMERIC(15)    NOT NULL,
    expiry_date    DATE           NOT NULL,
    due_date       DATE,
    customer_id    BIGINT         NOT NULL,
    CONSTRAINT customer_credit
        FOREIGN KEY (customer_id)
            REFERENCES "rp0-bankapp".customers (customer_id)
);
-- Create index on customer_id for faster credit card lookups by customer
CREATE INDEX idx_credit_cards_customer_id ON "rp0-bankapp".credit_cards (customer_id);

-- Base transaction table with common fields (Not yet implemented)
CREATE TABLE "rp0-bankapp".transactions_v2
(
    transaction_id     BIGSERIAL PRIMARY KEY,
    transaction_type   VARCHAR(20)    NOT NULL,
    transaction_amount NUMERIC(15, 2) NOT NULL CHECK (transaction_amount > 0),
    transaction_notes  VARCHAR(100)   NOT NULL,
    transaction_date   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    entity_type        VARCHAR(20)    NOT NULL, -- 'ACCOUNT', 'LOAN', or 'CREDIT'
    entity_id          BIGINT         NOT NULL
);

-- Create index on transaction_date for faster date-based queries
CREATE INDEX idx_transactions_date ON "rp0-bankapp".transactions_v2 (transaction_date);
-- Create index on entity_type and entity_id for faster lookups
CREATE INDEX idx_transactions_entity ON "rp0-bankapp".transactions_v2 (entity_type, entity_id);

-- Account transactions view for backward compatibility
CREATE VIEW "rp0-bankapp".account_transactions AS
SELECT t.transaction_id,
       t.transaction_type,
       t.transaction_amount,
       t.transaction_notes,
       t.transaction_date,
       t.entity_id as account_number
FROM "rp0-bankapp".transactions_v2 t
WHERE t.entity_type = 'ACCOUNT';

-- Loan transactions view for backward compatibility
CREATE VIEW "rp0-bankapp".loan_transactions AS
SELECT t.transaction_id,
       t.transaction_type,
       t.transaction_amount,
       t.transaction_notes,
       t.transaction_date,
       t.entity_id as loan_id
FROM "rp0-bankapp".transactions_v2 t
WHERE t.entity_type = 'LOAN';

-- Credit card transactions view for backward compatibility
CREATE VIEW "rp0-bankapp".credit_transactions AS
SELECT t.transaction_id,
       t.transaction_type,
       t.transaction_amount,
       t.transaction_notes,
       t.transaction_date,
       t.entity_id as credit_id
FROM "rp0-bankapp".transactions_v2 t
WHERE t.entity_type = 'CREDIT';

-- Transfers table
create table "rp0-bankapp".transfers
(
    transfer_id     SERIAL primary key,
    source_acct_num bigint         not null,
    target_acct_num bigint         not null,
    transfer_amount decimal(15, 2) not null,
    transfer_notes  varchar(100),
    created_at      timestamp      not null default (now() at time zone 'America/Chicago'),
    constraint src_account_fk foreign key (source_acct_num) references "rp0-bankapp".accounts (account_number),
    constraint tgt_account_fk foreign key (target_acct_num) references "rp0-bankapp".accounts (account_number),
    -- ensures source/target accounts are different
    check (source_acct_num is distinct from target_acct_num)
);
-- Create index on transfer_id for faster account lookups by transaction
CREATE INDEX idx_transfer_id ON "rp0-bankapp".transfers (transfer_id);
-- Create index on created_at for faster date-based queries
CREATE INDEX idx_transfers_created_at ON "rp0-bankapp".transfers (created_at);
-- Create indexes on source and target account numbers for faster lookups
CREATE INDEX idx_transfers_source ON "rp0-bankapp".transfers (source_acct_num);
CREATE INDEX idx_transfers_target ON "rp0-bankapp".transfers (target_acct_num);

-- Add check constraints for data integrity
ALTER TABLE "rp0-bankapp".accounts
    ADD CONSTRAINT chk_account_balance CHECK (account_balance >= 0);
ALTER TABLE "rp0-bankapp".loans
    ADD CONSTRAINT chk_loan_amount CHECK (loan_amount > 0);
ALTER TABLE "rp0-bankapp".loans
    ADD CONSTRAINT chk_loan_interest CHECK (interest >= 0);
ALTER TABLE "rp0-bankapp".credit_cards
    ADD CONSTRAINT chk_credit_limit CHECK (credit_limit > 0);