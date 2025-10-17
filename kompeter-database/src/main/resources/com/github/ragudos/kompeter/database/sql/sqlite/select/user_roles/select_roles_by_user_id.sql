SELECT
    GROUP_CONCAT(r.role_name, ', ') AS user_roles
FROM
    users u
JOIN
    user_roles ur
    ON
        u._user_id = ur._user_id
JOIN
    roles r
    ON
        ur._role_id = r._role_id
WHERE
    u._user_id = ?
GROUP BY
    u._user_id, u.display_name;

