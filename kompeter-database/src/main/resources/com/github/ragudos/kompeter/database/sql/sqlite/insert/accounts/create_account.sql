INSERT INTO accounts(
    _user_id,
    email,
    password_hash,
    password_salt
)
VALUES(
:_user_id,
:email,
:password_hash,
:password_salt
);
