/* SQL schema for p0 bank app remake */


create table customers (
	customer_id number primary key,
	first_name varchar2(50),
	last_name varchar2(50),
	address varchar2(50),
	username varchar2(50),
	passwrd varchar2(50)
);

create table accounts (
	account_number number primary key, --account number is the unique key
	account_type varchar(20) not null,
	account_balance decimal(15,2)
	foreign key (customer_id) references customers(customer_id) --not sure if these need to be on separate lines
	-- customer_id references customers
	
);

create table transactions (
	transaction_id number primary key,
	transaction_type varchar2(20) not null,
	transaction_balance decimal(15,2),
	transaction_notes varchar2(100),
	transaction_date date
	foreign key (account_id) references accounts(account_id)
	-- account_id references accounts
);

--potential 4th entities

create table loans ();

create table credit_card ();

create table banker_info ();
