package com.evolutionnext.features.account.infrastructure.adapter.out;

import com.evolutionnext.features.account.domain.model.Account;
import com.evolutionnext.features.account.domain.model.AccountId;
import com.evolutionnext.features.account.port.out.AccountRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryAccountRepository implements AccountRepository {
    private final Map<AccountId, Account> accountsById = new ConcurrentHashMap<>();
    private final Map<String, AccountId> accountIdsByUserName = new ConcurrentHashMap<>();

    @Override
    public void save(Account account) {
        accountsById.put(account.accountId(), account);
        accountIdsByUserName.put(account.userName().value(), account.accountId());
    }

    @Override
    public Optional<Account> findById(AccountId accountId) {
        return Optional.ofNullable(accountsById.get(accountId));
    }

    @Override
    public Optional<Account> findByUserName(String userName) {
        return Optional.ofNullable(accountIdsByUserName.get(userName))
            .flatMap(this::findById);
    }
}
