SELECT
    password_hash, password_salt
FROM
    accounts
WHERE
    email = ?;
