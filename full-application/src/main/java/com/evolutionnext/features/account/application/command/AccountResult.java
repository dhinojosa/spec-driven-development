package com.evolutionnext.features.account.application.command;

import com.evolutionnext.features.account.domain.model.AccountId;

public sealed interface AccountResult permits AccountResult.AccountRegistered, AccountResult.LogInSucceeded,
    AccountResult.LogInFailed, AccountResult.UserNameAlreadyExists {
    record AccountRegistered(AccountId accountId, String userName) implements AccountResult {
    }

    record LogInSucceeded(AccountId accountId, String userName) implements AccountResult {
    }

    record LogInFailed(String userName) implements AccountResult {
    }

    record UserNameAlreadyExists(String userName) implements AccountResult {
    }
}
