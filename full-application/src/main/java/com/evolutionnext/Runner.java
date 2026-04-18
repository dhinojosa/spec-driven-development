package com.evolutionnext;

import com.evolutionnext.features.account.infrastructure.adapter.out.InMemoryAccountRepository;
import com.evolutionnext.features.account.infrastructure.adapter.out.JdbcAccountRepository;
import com.evolutionnext.features.account.port.out.AccountRepository;

public final class Runner {
    private Runner() {
    }

    public static void main(String[] args) {
        var port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        new AccountApplication().start(port, accountRepository());
    }

    private static AccountRepository accountRepository() {
        var databaseUrl = System.getenv("DATABASE_URL");
        if (databaseUrl == null || databaseUrl.isBlank()) {
            return new InMemoryAccountRepository();
        }
        return new JdbcAccountRepository(databaseUrl,
            System.getenv().getOrDefault("DATABASE_USERNAME", "postgres"),
            System.getenv().getOrDefault("DATABASE_PASSWORD", "postgres"));
    }
}
