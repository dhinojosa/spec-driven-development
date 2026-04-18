package com.evolutionnext.features.account.arbitrary;

import com.evolutionnext.features.account.domain.model.Account;
import com.evolutionnext.features.account.domain.model.AccountId;
import com.evolutionnext.features.account.domain.model.PasswordCredential;
import com.evolutionnext.features.account.domain.model.UserName;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;

public final class AccountArbitrary {
    private AccountArbitrary() {
    }

    public static Arbitrary<String> userNames() {
        return Arbitraries.strings()
            .alpha()
            .ofMinLength(3)
            .ofMaxLength(20)
            .map(value -> "user-" + value);
    }

    public static Arbitrary<String> passwords() {
        return Arbitraries.strings()
            .alpha()
            .numeric()
            .ofMinLength(8)
            .ofMaxLength(32);
    }

    public static Arbitrary<Account> accounts() {
        return Combinators.combine(userNames(), passwords())
            .as((userName, password) -> Account.register(AccountId.newId(),
                new UserName(userName),
                new PasswordCredential(password)));
    }
}
