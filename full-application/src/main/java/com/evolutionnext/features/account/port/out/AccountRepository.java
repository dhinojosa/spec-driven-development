package com.evolutionnext.features.account.port.out;

import com.evolutionnext.features.account.domain.model.Account;
import com.evolutionnext.features.account.domain.model.AccountId;

import java.util.Optional;

public interface AccountRepository {
    void save(Account account);

    Optional<Account> findById(AccountId accountId);

    Optional<Account> findByUserName(String userName);
}
