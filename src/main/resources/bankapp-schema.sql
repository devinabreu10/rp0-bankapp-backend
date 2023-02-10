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
	transaction_balance decimal(15,2) not null,
	transaction_notes varchar2(100),
	transaction_date date not null,
    account_number number(15) not null,
    constraint account_transaction
	  foreign key (account_number) 
      references accounts(account_number)
	-- account_id references accounts
);

--potential 4th entities

--create table loans ();

--create table credit_card ();

--create table banker_info ();

COMMIT;