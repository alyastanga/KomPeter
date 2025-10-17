SELECT
    _user_id,
    _created_at,
    display_name,
    first_name,
    last_name
FROM
    users
WHERE
    _user_id = (
        SELECT
            _user_id
        FROM
            accounts
        WHERE
            email = ?
    );
