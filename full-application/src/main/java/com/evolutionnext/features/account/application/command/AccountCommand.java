package com.evolutionnext.features.account.application.command;

import com.evolutionnext.features.account.domain.model.AccountId;

public sealed interface AccountCommand permits AccountCommand.RegisterAccount, AccountCommand.LogIn {
    record RegisterAccount(AccountId accountId, String userName, String password) implements AccountCommand {
    }

    record LogIn(String userName, String password) implements AccountCommand {
    }
}
