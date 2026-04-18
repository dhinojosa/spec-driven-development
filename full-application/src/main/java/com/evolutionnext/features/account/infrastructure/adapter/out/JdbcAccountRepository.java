package com.evolutionnext.features.account.infrastructure.adapter.out;

import com.evolutionnext.features.account.domain.model.Account;
import com.evolutionnext.features.account.domain.model.AccountId;
import com.evolutionnext.features.account.domain.model.PasswordCredential;
import com.evolutionnext.features.account.domain.model.UserName;
import com.evolutionnext.features.account.port.out.AccountRepository;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public final class JdbcAccountRepository implements AccountRepository {
    private final String jdbcUrl;
    private final String username;
    private final String password;

    public JdbcAccountRepository(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        initialize();
    }

    @Override
    public void save(Account account) {
        var sql = """
            insert into accounts(account_id, user_name, password_value)
            values (?, ?, ?)
            on conflict (account_id) do update
            set user_name = excluded.user_name, password_value = excluded.password_value
            """;
        try (var connection = DriverManager.getConnection(jdbcUrl, username, password);
             var statement = connection.prepareStatement(sql)) {
            statement.setObject(1, account.accountId().value());
            statement.setString(2, account.userName().value());
            statement.setString(3, account.passwordCredential().value());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to save account", exception);
        }
    }

    @Override
    public Optional<Account> findById(AccountId accountId) {
        return find("select account_id, user_name, password_value from accounts where account_id = ?", accountId.value());
    }

    @Override
    public Optional<Account> findByUserName(String userName) {
        return find("select account_id, user_name, password_value from accounts where user_name = ?", userName);
    }

    private Optional<Account> find(String sql, Object value) {
        try (var connection = DriverManager.getConnection(jdbcUrl, username, password);
             var statement = connection.prepareStatement(sql)) {
            statement.setObject(1, value);
            try (var resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(Account.restore(new AccountId((UUID) resultSet.getObject("account_id")),
                    new UserName(resultSet.getString("user_name")),
                    new PasswordCredential(resultSet.getString("password_value"))));
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to find account", exception);
        }
    }

    private void initialize() {
        var sql = """
            create table if not exists accounts(
                account_id uuid primary key,
                user_name text not null unique,
                password_value text not null
            )
            """;
        try (var connection = DriverManager.getConnection(jdbcUrl, username, password);
             var statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException exception) {
            throw new IllegalStateException("Unable to initialize account repository", exception);
        }
    }
}
