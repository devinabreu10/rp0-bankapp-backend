/* SQL schema for p0 bank app remake */


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