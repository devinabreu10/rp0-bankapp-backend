SELECT prosrc FROM pg_proc WHERE proname = 'transfer_account_funds';

CALL "rp0-bankapp".transfer_account_funds(16, 17, 100.00, 'test notes');

-- Creating stored procedure for transferring funds between accounts
CREATE PROCEDURE "rp0-bankapp".transfer_account_funds(
    source_account_number BIGINT,
    target_account_number BIGINT,
    amount DECIMAL(15, 2),
    notes VARCHAR(100)
)
LANGUAGE plpgsql
AS $$
DECLARE
    source_balance DECIMAL(15, 2);   -- Variable to hold sender's balance
BEGIN
	-- Check sender's balance
    SELECT account_balance INTO source_balance
    FROM "rp0-bankapp".accounts
    WHERE account_number = source_account_number;

    -- Validate if the sender has sufficient funds
    IF source_balance < amount THEN
        RAISE EXCEPTION 'Insufficient funds for transfer from account %', source_account_number
        USING ERRCODE = 'P0001';  -- Custom error code
    END IF;
	
    -- sender's account
    UPDATE "rp0-bankapp".accounts
    SET account_balance = account_balance - amount, updated_at = (NOW() AT TIME ZONE 'AMERICA/CHICAGO')
    WHERE account_number = source_account_number;

    -- receiver's account
    UPDATE "rp0-bankapp".accounts
    SET account_balance = account_balance + amount, updated_at = (NOW() AT TIME ZONE 'AMERICA/CHICAGO')
    WHERE account_number = target_account_number;

	-- Insert transfer record
    INSERT INTO "rp0-bankapp".transfers (source_acct_num, target_acct_num, transfer_amount, transfer_notes)
    VALUES (source_account_number, target_account_number, amount, notes);

    -- Check for errors
    IF FOUND THEN
        COMMIT;
    ELSE
        ROLLBACK;
    END IF;
END;
$$;