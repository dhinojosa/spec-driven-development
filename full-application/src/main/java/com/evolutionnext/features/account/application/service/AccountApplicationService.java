package com.evolutionnext.features.account.application.service;

import com.evolutionnext.features.account.application.command.AccountCommand;
import com.evolutionnext.features.account.application.command.AccountResult;
import com.evolutionnext.features.account.application.query.AccountQueryResult;
import com.evolutionnext.features.account.domain.model.Account;
import com.evolutionnext.features.account.domain.model.AccountId;
import com.evolutionnext.features.account.domain.model.PasswordCredential;
import com.evolutionnext.features.account.domain.model.UserName;
import com.evolutionnext.features.account.port.in.AnonymousAccountCommandPort;
import com.evolutionnext.features.account.port.in.AnonymousAccountQueryPort;
import com.evolutionnext.features.account.port.out.AccountRepository;

public final class AccountApplicationService implements AnonymousAccountCommandPort, AnonymousAccountQueryPort {
    private final AccountRepository accountRepository;

    public AccountApplicationService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountResult execute(AccountCommand command) {
        return switch (command) {
            case AccountCommand.RegisterAccount registerAccount -> register(registerAccount);
            case AccountCommand.LogIn logIn -> logIn(logIn);
        };
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

    private AccountResult register(AccountCommand.RegisterAccount registerAccount) {
        if (accountRepository.findByUserName(registerAccount.userName()).isPresent()) {
            return new AccountResult.UserNameAlreadyExists(registerAccount.userName());
        }
        var account = Account.register(registerAccount.accountId(),
            new UserName(registerAccount.userName()),
            new PasswordCredential(registerAccount.password()));
        accountRepository.save(account);
        return new AccountResult.AccountRegistered(account.accountId(), account.userName().value());
    }

    private AccountResult logIn(AccountCommand.LogIn logIn) {
        return accountRepository.findByUserName(logIn.userName())
            .map(account -> {
                if (account.authenticate(logIn.password())) {
                    accountRepository.save(account);
                    return (AccountResult) new AccountResult.LogInSucceeded(account.accountId(), account.userName().value());
                }
                return new AccountResult.LogInFailed(logIn.userName());
            })
            .orElseGet(() -> new AccountResult.LogInFailed(logIn.userName()));
    }
}
