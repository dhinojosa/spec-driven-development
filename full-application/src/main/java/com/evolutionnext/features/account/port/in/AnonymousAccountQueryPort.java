package com.evolutionnext.features.account.port.in;

import com.evolutionnext.features.account.application.query.AccountQueryResult;
import com.evolutionnext.features.account.domain.model.AccountId;

public interface AnonymousAccountQueryPort {
    AccountQueryResult findById(AccountId accountId);

    AccountQueryResult findByUserName(String userName);
}
