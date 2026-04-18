package com.evolutionnext.features.account.application.query;

import com.evolutionnext.features.account.domain.model.AccountId;

public sealed interface AccountQueryResult permits AccountQueryResult.AccountFound, AccountQueryResult.AccountNotFound {
    record AccountFound(AccountId accountId, String userName) implements AccountQueryResult {
    }

    record AccountNotFound(String lookup) implements AccountQueryResult {
    }
}
