/* SQL schema for p0 bank app remake in Oracle */

create table customer ( --added trigger to auto increment customer_id
	customer_id number(15) primary key,
	first_name varchar2(50) not null,
	last_name varchar2(50) not null,
	address varchar2(50) not null,
	username varchar2(50) not null,
	passwrd varchar2(50) not null
);

create table accounts (
	account_number number(15) primary key, --account number is the unique key
	account_type varchar(20) not null,
	account_balance decimal(15,2) not null,
    customer_id number(15) not null,
	constraint customer_account 
      foreign key (customer_id) 
      references customer (customer_id) --not sure if these need to be on separate lines
	-- customer_id references customers
);

create table transactions (
	transaction_id number(15) primary key,
	transaction_type varchar2(20) not null,
	transaction_amount decimal(15,2) not null,
	transaction_notes varchar2(100) not null,
	transaction_date date not null,
    account_number number(15),
	loan_id number(15),
	credit_id number(15),
    constraint account_transaction
	  foreign key (account_number) 
      references accounts (account_number),
	constraint loan_payment
	  foreign key (loan_id) 
      references loan (loan_id),
	constraint credit_payment
	  foreign key (credit_id) 
      references credit (credit_id)
	-- account_id references accounts
);

create table loan ( -- establish in the code how much of the loan the customer owes and what's left
	loan_id number(15) primary key,
	loan_amount decimal(15,2) not null, --can't open a $0 loan
	loan_length number(5) not null,
	interest decimal (15,2) not null,
	minimum_due decimal(15,2), --based off interest
	due_date date not null,
	customer_id number(15) not null,
	constraint customer_loan
	  foreign key (customer_id) 
      references customer (customer_id)
);

create table credit ( -- this credit has no interest
	credit_id number(15) primary key,
	credit_balance decimal(15,2) not null,
	minimum_due decimal(15,2), --20% due every month or something
	credit_limit number(15) not null,
	expiry_date date not null, --when credit is opened + set amt of time
	due_date date,
	customer_id number(15) not null,
	constraint customer_credit
	  foreign key (customer_id) 
      references customer (customer_id)
);

COMMIT;

/* SQL schema for p0 bank app remake in PostgreSQL */

create table "rp0-bankapp".customers (
	customer_id bigserial primary key, -- automatically increments in PostgreSQL
	first_name varchar(50) not null,
	last_name varchar(50) not null,
	address varchar(50) not null,
	username varchar(50) not null,
	passwrd varchar(50) not null
);

create table "rp0-bankapp".accounts (
	account_number bigserial primary key, -- automatically increments in PostgreSQL
	account_type varchar(20) not null,
	account_balance numeric(15,2) not null,
    customer_id bigint not null,
	constraint customer_account 
      foreign key (customer_id) 
      references "rp0-bankapp".customers (customer_id) 
);

create table "rp0-bankapp".transactions (
	transaction_id bigserial primary key,
	transaction_type varchar(20) not null,
	transaction_amount numeric(15,2) not null,
	transaction_notes varchar(100) not null,
	transaction_date date not null,
    account_number bigint,
	loan_id bigint,
	credit_id bigint,
    constraint account_transaction
	  foreign key (account_number) 
      references "rp0-bankapp".accounts (account_number),
	constraint loan_payment
	  foreign key (loan_id) 
      references "rp0-bankapp".loans (loan_id),
	constraint credit_payment
	  foreign key (credit_id) 
      references "rp0-bankapp".credit_cards (credit_id)
);

create table "rp0-bankapp".transfers (
    transfer_id bigserial primary key,
    source_acct_num bigint not null,
    target_acct_num bigint not null,
    transfer_amount numeric(15,2) not null,
	transfer_notes varchar(150),
    transfer_date date not null,
    constraint src_account_fk foreign key (source_acct_num) references "rp0-bankapp".accounts (account_number),
    constraint tgt_account_fk foreign key (target_acct_num) references "rp0-bankapp".accounts (account_number),
	-- ensures source/target accounts are different
    check (source_acct_num is distinct from target_acct_num)
);

create table "rp0-bankapp".loans (
	loan_id bigserial primary key,
	loan_amount numeric(15,2) not null,
	loan_length integer not null,
	interest numeric (15,2) not null,
	minimum_due numeric(15,2),
	due_date date not null,
	customer_id bigint not null,
	constraint customer_loan
	  foreign key (customer_id) 
      references "rp0-bankapp".customers (customer_id)
);

create table "rp0-bankapp".credit_cards (
	credit_id bigserial primary key,
	credit_balance numeric(15,2) not null,
	minimum_due numeric(15,2),
	credit_limit numeric(15) not null,
	expiry_date date not null,
	due_date date,
	customer_id bigint not null,
	constraint customer_credit
	  foreign key (customer_id) 
      references "rp0-bankapp".customers (customer_id)
);
-- Postgres doesn't require explicit COMMIT
