SELECT
    EXISTS(
        SELECT
            1
        FROM
            users
        WHERE
            display_name = ?
    );
