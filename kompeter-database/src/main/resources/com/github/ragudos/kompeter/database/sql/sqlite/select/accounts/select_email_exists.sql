SELECT
    EXISTS(
        SELECT
            1
        FROM
            accounts
        WHERE
            email = ?
    );
