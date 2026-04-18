create table if not exists accounts(
    account_id uuid primary key,
    user_name text not null unique,
    password_value text not null
);
