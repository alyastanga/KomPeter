SELECT
    _session_id,
    _created_at,
    expires_at,
    _user_id,
    session_token,
    ip_address
FROM
    sessions
WHERE
    _user_id = (
        SELECT
            _user_id
        FROM
            users
        WHERE
            _user_id = ?
    );
