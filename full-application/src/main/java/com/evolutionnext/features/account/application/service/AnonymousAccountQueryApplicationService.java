package com.evolutionnext.features.account.application.service;

import com.evolutionnext.features.account.application.query.AccountQueryResult;
import com.evolutionnext.features.account.domain.model.AccountId;
import com.evolutionnext.features.account.port.in.AnonymousAccountQueryPort;
import com.evolutionnext.features.account.port.out.AccountRepository;

public final class AnonymousAccountQueryApplicationService implements AnonymousAccountQueryPort {
    private final AccountRepository accountRepository;

    public AnonymousAccountQueryApplicationService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountQueryResult findById(AccountId accountId) {
        return accountRepository.findById(accountId)
            .<AccountQueryResult>map(account -> new AccountQueryResult.AccountFound(account.accountId(),
                account.userName().value()))
            .orElseGet(() -> new AccountQueryResult.AccountNotFound(accountId.value().toString()));
    }

    @Override
    public AccountQueryResult findByUserName(String userName) {
        return accountRepository.findByUserName(userName)
            .<AccountQueryResult>map(account -> new AccountQueryResult.AccountFound(account.accountId(),
                account.userName().value()))
            .orElseGet(() -> new AccountQueryResult.AccountNotFound(userName));
    }
}
