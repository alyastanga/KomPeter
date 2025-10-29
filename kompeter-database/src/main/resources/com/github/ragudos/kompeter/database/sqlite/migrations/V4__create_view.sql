CREATE VIEW
    user_metadata
AS
    SELECT
        u._user_id
            AS _user_id,
        a.email,
        u.display_name,
        u.first_name,
        u.last_name,
        u._created_at AS _created_at,
        GROUP_CONCAT(r.role_name, ',') AS roles
    FROM
        users u
    LEFT JOIN
        accounts a
        ON
            a._user_id = u._user_id
    LEFT JOIN
        user_roles ur
        ON
            ur._user_id = u._user_id
    LEFT JOIN
        roles r
        ON
            r._role_id = ur._role_id
    GROUP BY
        u._user_id, a.email, u.display_name, u.first_name, u.last_name, u._created_at;
